package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRoomRepository extends Repository<ChatRoom> {

    private static ChatRoomRepository instance = new ChatRoomRepository();

    // Needed for getPrivateChat().
    private static TravelerRepository travelerInstance = new TravelerRepository();

    protected ChatRoomRepository() {
        super("chat_room", new String[] {
                "id", "from", "to", "created_on"
        });
    }

    public static ChatRoomRepository getInstance() {
        return ChatRoomRepository.instance;
    }

    @Override
    protected ChatRoom convertToBusinessObject(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int fromId = rs.getInt(2);
        int toId = rs.getInt(3);
        Date createdOn = rs.getDate(4);

        // TODO find a better to get different chat types

        ArrayList<Message> messages = MessageRepository.getInstance().getAllMessagesForChat(id);
        ArrayList<Traveler> travelers = TravelerRepository.getInstance().getAllTravelersForChat(id);
        Station from = StationRepository.getInstance().getById(fromId);
        Station to = StationRepository.getInstance().getById(toId);

        if (from == null || to == null)
            return new PrivateChat(id, createdOn, travelers, messages);
        else
            return new PublicChat(id, from, to, createdOn, travelers, messages);
    }

    @Override
    protected void prepareStatement(PreparedStatement statement, ChatRoom object) throws SQLException {
        // TODO find a better way to handle different chat types
        if (object instanceof PrivateChat) {
            PrivateChat c = (PrivateChat)object;
            statement.setNull(1, Types.VARCHAR);
            statement.setNull(2, Types.VARCHAR);
            statement.setTimestamp(3, new java.sql.Timestamp(object.getCreatedOn().getTime()));
        } else if (object instanceof PublicChat) {
            PublicChat c = (PublicChat)object;
            statement.setInt(1, c.getFrom().getId());
            statement.setInt(2, c.getTo().getId());
            statement.setTimestamp(3, new java.sql.Timestamp(object.getCreatedOn().getTime()));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int add(ChatRoom o) {
        // save station 1:n relations and update model ids
        o = save_station_relations(o);

        // save chatRoom before we persist mm relations
        int roomId = super.add(o);
        o.setId(roomId);

        save_traveler_relations(o);

        return roomId;
    }

    public int update(ChatRoom o) {

        // lazy way to update mm relations
        delete_traveler_relations(o);
        save_traveler_relations(o);

        return super.update(o);
    }

    private ChatRoom save_station_relations(ChatRoom o) {
        if (o instanceof PublicChat) {
            Station from = ((PublicChat) o).getFrom();
            Station to = ((PublicChat) o).getTo();

            int fromId = StationRepository.getInstance().save(from);
            int toId = StationRepository.getInstance().save(to);

            from.setId(fromId);
            to.setId(toId);

            ((PublicChat) o).setFrom(from);
            ((PublicChat) o).setTo(to);
        }
        return o;
    }

    private void save_traveler_relations(ChatRoom o) {
        List<Traveler> travelers = o.getAllTravelers();
        if (travelers != null) {
            String[] fields = new String[] {"chat_room_id", "traveler_id"};
            String sql = this.getQueryBuilder().generateInsertQuery("chat_room_traveler", fields);
            for (Traveler traveler : travelers) {

                try {
                    PreparedStatement p = this.getDbConnection().prepareStatement(sql);
                    p.setInt(1, o.getId());
                    p.setInt(2, traveler.getId());
                    p.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void delete_traveler_relations(ChatRoom o) {
        String[] fields = new String[] {"chat_room_id"};
        String sql = this.getQueryBuilder().generateDeleteQuery("chat_room_traveler", fields);
        try {
            PreparedStatement p = this.getDbConnection().prepareStatement(sql);
            p.setInt(1, o.getId());
            p.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<ChatRoom> getMostPopular(int limit) {
        String sql = "SELECT " + this.getQueryBuilder().getFieldsAsStringForSelectWithPrefix("c") +
                        ", (SELECT COUNT(id) from chat_room_traveler as ct WHERE ct.chat_room_id  = c.id) AS trvlr_count " +
                    " FROM " + this.getTableName() + " as c" +
                    " WHERE `from` IS NOT NULL" +
                    " AND `to` IS NOT NULL" +
                    " ORDER BY trvlr_count DESC" +
                    " LIMIT ? ";

        try {
            PreparedStatement p = this.getDbConnection().prepareStatement(sql);
            p.setInt(1, limit);
            return getList(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<ChatRoom> findChatRoomsForConnection(String from, String to) {
        String sql = "SELECT " + this.getQueryBuilder().getFieldsAsStringForSelectWithPrefix("c") +
                     " FROM " + this.getTableName() + " as c, station as s " +
                     " WHERE  c.`from` = s.`id` AND s.`name` = ?" +
                     " AND c.`to` IN (SELECT `id` FROM station WHERE `name` = ?)";

        try {
            PreparedStatement p = this.getDbConnection().prepareStatement(sql);
            p.setString(1, from);
            p.setString(2, to);
            return getList(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<ChatRoom> getByTravelerId(int travelerId) {
        String sql = "SELECT " + this.getQueryBuilder().getFieldsAsStringForSelectWithPrefix("t") +
                " FROM " + this.getTableName() + " as t " +
                " JOIN chat_room_traveler as c ON t.`id` = c.`chat_room_id` AND c.`traveler_id` = ?";

        try {
            PreparedStatement p = this.getDbConnection().prepareStatement(sql);
            p.setInt(1, travelerId);
            return getList(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get a private chat for two users.
     * If the private chat room does not exist yet, one will be created.
     * Per two users only one room will be created. This is achieved by ensuring that userId1 is always
     * LOWER than userId2.
     */
    public ChatRoom getPrivateChat(int userId1, int userId2) {
        // Ensure that userId1 is lower than userId2.
        if (userId1 > userId2) {
            int swap = userId2;
            userId2 = userId1;
            userId1 = swap;
        }

        // TODO: Refactor this.
        String sql = ""
            + " SELECT r.id"
            + " FROM chat_room AS r"
            + " JOIN ("
                + " SELECT sub1.chat_room_id"
                + " FROM chat_room_traveler sub1"
                + " WHERE sub1.traveler_id = ?"
            + " ) p1 ON p1.chat_room_id = r.id"
            + " JOIN ("
                + " SELECT sub2.chat_room_id"
                + " FROM chat_room_traveler sub2"
                + " WHERE sub2.traveler_id = ?"
            + " ) p2 ON p2.chat_room_id = r.id"
            + " WHERE"
            + " `from` IS NULL"
            + " AND `to` IS NULL";

        try {
            PreparedStatement p = this.getDbConnection().prepareStatement(sql);
            p.setInt(1, userId1);
            p.setInt(2, userId2);

            ResultSet rs = p.executeQuery();

            if (rs.isBeforeFirst() && rs.next()) {
                // Private chat room already exists, return this chat room.
                return this.getById(rs.getInt("id"));
            } else {
                // Private chat room doesn't exist yet, create it.
                Traveler t1 = travelerInstance.getById(userId1);
                Traveler t2 = travelerInstance.getById(userId2);
                PrivateChat room = (PrivateChat) new PrivateChat(t1, t2);
                this.save(room);

                return room;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatRoomRepository extends Repository<ChatRoom> {

	private static ChatRoomRepository instance = new ChatRoomRepository();

	// Needed for getPrivateChat().
	private static TravelerRepository travelerInstance;

	protected ChatRoomRepository() {
		super("chat_room", new String[]{
				"id", "from", "to", "created_on"
		});

		travelerInstance =  TravelerRepository.getInstance();
	}

	/**
	 * Get the current ChatRoomRepository instance
	 *
	 * @return ChatRoomRepository
	 */
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

		// Don't load messages for now, to prevent memory issues
		// ArrayList<Message> messages = MessageRepository.getInstance().getAllMessagesForChat(id);

		ArrayList<Message> messages = new ArrayList<>();
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
			PrivateChat c = (PrivateChat) object;
			statement.setNull(1, Types.VARCHAR);
			statement.setNull(2, Types.VARCHAR);
			statement.setTimestamp(3, new java.sql.Timestamp(object.getCreatedOn().getTime()));
		} else if (object instanceof PublicChat) {
			PublicChat c = (PublicChat) object;
			statement.setInt(1, c.getFrom().getId());
			statement.setInt(2, c.getTo().getId());
			statement.setTimestamp(3, new java.sql.Timestamp(object.getCreatedOn().getTime()));
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Add a new chat room to the database
	 * <p>
	 * All relations will be created and on success the id of the new database entry will be returned,
	 * 0 otherwise.
	 *
	 * @param o ChatRoom
	 * @return int
	 */
	public int add(ChatRoom o) {
		// save station 1:n relations and update model ids
		o = save_station_relations(o);

		// save chatRoom before we persist mm relations
		int roomId = super.add(o);
		o.setId(roomId);

		save_traveler_relations(o);

		return roomId;
	}

	/**
	 * Update an existing chat room in the database
	 *
	 * @param o ChatRoom
	 * @return int
	 */
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
		Connection conn = null;
		PreparedStatement p = null;

		List<Traveler> travelers = o.getAllTravelers();
		if (travelers != null) {
			String[] fields = new String[]{"chat_room_id", "traveler_id"};
			String sql = this.getQueryBuilder().generateInsertQuery("chat_room_traveler", fields);

			for (Traveler traveler : travelers) {
				try {
					conn = this.getDbConnection();
					p = conn.prepareStatement(sql);
					p.setInt(1, o.getId());
					p.setInt(2, traveler.getId());
					p.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					closeConnection(null, p, conn);
				}
			}
		}
	}

	private void delete_traveler_relations(ChatRoom o) {
		Connection conn = null;
		PreparedStatement p = null;
		String[] fields = new String[]{"chat_room_id"};
		String sql = this.getQueryBuilder().generateDeleteQuery("chat_room_traveler", fields);

		try {
			conn = getDbConnection();
			p = conn.prepareStatement(sql);
			p.setInt(1, o.getId());
			p.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(null, p, conn);
		}

	}

	/**
	 * Get the most popular chat rooms
	 * <p>
	 * Popularity is defined as number of participants
	 *
	 * @param limit int
	 * @return ArrayList<ChatRoom>
	 */
	public ArrayList<ChatRoom> getMostPopular(int limit) {
		Connection conn = null;
		PreparedStatement p = null;
		ArrayList<ChatRoom> chatRooms = null;
		String sql = "SELECT " + this.getQueryBuilder().getFieldsAsStringForSelectWithPrefix("c") +
				", (SELECT COUNT(id) from chat_room_traveler as ct WHERE ct.chat_room_id  = c.id) AS trvlr_count " +
				" FROM " + this.getTableName() + " as c" +
				" WHERE `from` IS NOT NULL" +
				" AND `to` IS NOT NULL" +
				" ORDER BY trvlr_count DESC" +
				" LIMIT ? ";

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql);
			p.setInt(1, limit);
			chatRooms = getList(p);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(null, p, conn);
		}

		return chatRooms;
	}

	/**
	 * Find chat rooms for connections
	 * <p>
	 * From and to have to already exist as stations in the db.
	 *
	 * @param from String
	 * @param to   String
	 * @return ArrayList<ChatRoom>
	 */
	public ArrayList<ChatRoom> findChatRoomsForConnection(String from, String to) {
		Connection conn = null;
		PreparedStatement p = null;
		String sql = "SELECT " + this.getQueryBuilder().getFieldsAsStringForSelectWithPrefix("c") +
				" FROM " + this.getTableName() + " as c, station as s " +
				" WHERE  c.`from` = s.`id` AND s.`name` = ?" +
				" AND c.`to` IN (SELECT `id` FROM station WHERE `name` = ?)";
		ArrayList<ChatRoom> chatRooms = null;

		try {
			conn = getDbConnection();
			p = conn.prepareStatement(sql);
			p.setString(1, from);
			p.setString(2, to);
			chatRooms = getList(p);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(null, p, conn);
		}

		return chatRooms;
	}

	/**
	 * Get chat rooms by traveler id
	 *
	 * @param travelerId int
	 * @return ArrayList<ChatRoom>
	 */
	public ArrayList<ChatRoom> getByTravelerId(int travelerId) {
		Connection conn = null;
		PreparedStatement p = null;
		ArrayList<ChatRoom> chatRooms = null;
		String sql = "SELECT " + this.getQueryBuilder().getFieldsAsStringForSelectWithPrefix("t") +
				" FROM " + this.getTableName() + " as t " +
				" JOIN chat_room_traveler as c ON t.`id` = c.`chat_room_id` AND c.`traveler_id` = ?" +
				" LEFT JOIN station s_from on s_from.id = t.`from`" +
				" LEFT JOIN station s_to on s_to.id = t.`to`" +
				" ORDER BY CONCAT(s_from.name, s_to.name) ASC";

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql);
			p.setInt(1, travelerId);
			chatRooms = getList(p);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(null, p, conn);
		}

		return chatRooms;
	}

	/**
	 * Get a private chat for two users.
	 * <p>
	 * If the private chat room does not exist yet, one will be created.
	 *
	 * @param travelerId1 int
	 * @param travelerId2 int
	 * @return ChatRoom
	 */
	public ChatRoom getOrCreatePrivateChat(int travelerId1, int travelerId2) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;
		ChatRoom chatRoom = null;
		String sql = "" +
				" SELECT DISTINCT r.id" +
				" FROM chat_room AS r" +
				"  JOIN chat_room_traveler AS ct1 ON ct1.chat_room_id = r.id AND ct1.traveler_id = ?" +
				"  JOIN chat_room_traveler AS ct2 ON ct2.chat_room_id = r.id AND ct2.traveler_id = ?" +
				" WHERE" +
				"  ct1.chat_room_id = ct2.chat_room_id" +
				"  AND `from` IS NULL" +
				"  AND `to` IS NULL";

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql);
			p.setInt(1, travelerId1);
			p.setInt(2, travelerId2);

			rs = p.executeQuery();

			if (rs.isBeforeFirst() && rs.next()) {
				// Private chat room already exists, return this chat room.
				chatRoom = this.getById(rs.getInt("id"));
			} else {
				// Private chat room doesn't exist yet, create it.
				Traveler t1 = travelerInstance.getById(travelerId1);
				Traveler t2 = travelerInstance.getById(travelerId2);

				chatRoom = new PrivateChat(t1, t2);
				int id = this.save(chatRoom);
				chatRoom.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(rs, p, conn);
		}

		return chatRoom;
	}
}

package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

public class ChatRoomRepository extends Repository<ChatRoom> {

    private static ChatRoomRepository instance = new ChatRoomRepository();

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
        String from = rs.getString(2);
        String to = rs.getString(3);
        Date createdOn = rs.getDate(4);

        // TODO find a better to get different chat types

        ArrayList<Message> messages = MessageRepository.getInstance().getAllMessagesForChat(id);
        ArrayList<Traveler> travelers = TravelerRepository.getInstance().getAllTravelersForChat(id);

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
            statement.setDate(2, (java.sql.Date) object.getCreatedOn());
        } else if (object instanceof PublicChat) {
            PublicChat c = (PublicChat)object;
            statement.setString(1, c.getFrom());
            statement.setString(2, c.getTo());
            statement.setDate(3, (java.sql.Date) object.getCreatedOn());
        } else {
            throw new IllegalArgumentException();
        }

    }

    public ArrayList<ChatRoom> findChatRoomsForConnection(String from, String to) { return null; }
}

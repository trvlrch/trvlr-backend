package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.Message;
import ch.trvlr.backend.model.Traveler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class MessageRepository extends Repository<Message> {

    private static MessageRepository instance = new MessageRepository();

    protected MessageRepository() {
        super("message", new String[] {
                "id", "author", "text", "timestamp", "chat_room_id"
        });
    }

    public static MessageRepository getInstance() {
        return MessageRepository.instance;
    }

    @Override
    protected Message convertToBusinessObject(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        Traveler author = TravelerRepository.getInstance().getById(rs.getInt(2));
        String text = rs.getString(3);
        Date timestamp = rs.getTimestamp(4);

        return new Message(id, author, text, timestamp);
    }

    @Override
    protected void prepareStatement(PreparedStatement statement, Message object) throws SQLException {
        statement.setInt(1,object.getAuthorId());
        statement.setString(2, object.getText());
        statement.setDate(3, (java.sql.Date) object.getTimestamp());
        statement.setInt(4, object.getChatRoomId());
    }

    public ArrayList<Message> getAllMessagesForChat(int chatId) throws SQLException {
        ArrayList<Message> result = new ArrayList<>();
        String sql = "SELECT " + this.getFieldsAsStringForSelect() +
                     " FROM " + this.getTableTame() +
                     " WHERE 'chat_room_id' = ?";

        PreparedStatement p = this.getDbConnection().prepareStatement(sql);
        p.setInt(1, chatId);

        ResultSet rs = p.executeQuery();
        while (rs.next()) {
            result.add(this.convertToBusinessObject(rs));
        }
        return result;
    }
}

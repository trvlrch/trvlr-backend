package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.Traveler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TravelerRepository extends Repository<Traveler> {

    private static TravelerRepository instance = new TravelerRepository();

    protected TravelerRepository() {
        super("Traveler", new String[] {
                    "id", "firstname", "lastname", "email", "auth_token"
                });
    }

    public static TravelerRepository getInstance() {
        return TravelerRepository.instance;
    }

    @Override
    protected Traveler convertToBusinessObject(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String firstName = rs.getString(2);
        String lastName = rs.getString(3);
        String eMail = rs.getString(4);
        String uid = rs.getString(5);

        return new Traveler(id, firstName, lastName, eMail, uid);
    }

    @Override
    protected void prepareStatement(PreparedStatement statement, Traveler object) throws SQLException {
        statement.setString(1, object.getFirstName());
        statement.setString(2, object.getLastName());
        statement.setString(3, object.getEmail());
        statement.setString(4, object.getUid());
    }

    public ArrayList<Traveler> getAllTravelersForChat(int chatId) throws SQLException {
        ArrayList<Traveler> result = new ArrayList<>();
        String sql = "SELECT " + this.getFieldsAsStringForSelectWithPrefix("t") +
                     " FROM " + this.getTableTame() + " as t, chat_room_traveler as c " +
                     " WHERE t.`id` = c.`traveler_id` AND c.`chat_room_id` = ?";

        PreparedStatement p = this.getDbConnection().prepareStatement(sql);
        p.setInt(1, chatId);

        ResultSet rs = p.executeQuery();
        while (rs.next()) {
            result.add(this.convertToBusinessObject(rs));
        }
        return result;
    }
}

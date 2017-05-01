package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.Traveler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TravelerRepository extends Repository<Traveler> {

    private static TravelerRepository instance = new TravelerRepository();

    protected TravelerRepository() {
        super("Traveler", new String[] {
                    "id", "firstname", "lastname", "email", "uid"
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

}

package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.Traveler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TravelerRepository extends Repository<Traveler> {

	private static TravelerRepository instance = new TravelerRepository();

	protected TravelerRepository() {
		super("traveler", new String[]{
				"id", "firstname", "lastname", "email", "uid"
		});
	}


	/**
	 * Get the current TravelerRepository instance
	 *
	 * @return TravelerRepository
	 */
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

	/**
	 * Get a traveler by their email
	 *
	 * @param email
	 * @return
	 */
	public Traveler getByEmail(String email) {
		String sql = this.getQueryBuilder().generateSelectQuery(new String[]{"email"});

		try {
			PreparedStatement p = this.getDbConnection().prepareStatement(sql);
			p.setString(1, email);
			return getSingle(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get a traveler by their firebase id
	 *
	 * @param firebaseId String
	 * @return Traveler
	 */
	public Traveler getByFirebaseId(String firebaseId) {
		String sql = this.getQueryBuilder().generateSelectQuery(new String[]{"uid"});

		try {
			PreparedStatement p = this.getDbConnection().prepareStatement(sql);
			p.setString(1, firebaseId);
			return getSingle(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get all travelers of a chat room
	 *
	 * @param chatId int
	 * @return ArrayList<Traveler>
	 */
	public ArrayList<Traveler> getAllTravelersForChat(int chatId) {
		String sql = "SELECT " + this.getQueryBuilder().getFieldsAsStringForSelectWithPrefix("t") +
				" FROM " + this.getTableName() + " as t, chat_room_traveler as c " +
				" WHERE t.`id` = c.`traveler_id` AND c.`chat_room_id` = ?";

		try {
			PreparedStatement p = this.getDbConnection().prepareStatement(sql);
			p.setInt(1, chatId);
			return getList(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}

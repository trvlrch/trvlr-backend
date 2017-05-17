package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.Traveler;

import java.sql.*;
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
		Connection conn = null;
		PreparedStatement p = null;
		String sql = this.getQueryBuilder().generateSelectQuery(new String[]{"email"});
		Traveler traveler = null;

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql);
			p.setString(1, email);
			traveler = getSingle(p);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(null, p, conn);
		}

		return traveler;
	}

	/**
	 * Get a traveler by their firebase id
	 *
	 * @param firebaseId String
	 * @return Traveler
	 */
	public Traveler getByFirebaseId(String firebaseId) {
		Connection conn = null;
		PreparedStatement p = null;
		String sql = this.getQueryBuilder().generateSelectQuery(new String[]{"uid"});
		Traveler traveler = null;

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql);
			p.setString(1, firebaseId);
			traveler = getSingle(p);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(null, p, conn);
		}

		return traveler;
	}

	/**
	 * Get all travelers of a chat room
	 *
	 * @param chatId int
	 * @return ArrayList<Traveler>
	 */
	public ArrayList<Traveler> getAllTravelersForChat(int chatId) {
		Connection conn = null;
		PreparedStatement p = null;
		String sql = "SELECT " + this.getQueryBuilder().getFieldsAsStringForSelectWithPrefix("t") +
				" FROM " + this.getTableName() + " as t, chat_room_traveler as c " +
				" WHERE t.`id` = c.`traveler_id` AND c.`chat_room_id` = ?";
		ArrayList<Traveler> travelers = null;

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql);
			p.setInt(1, chatId);
			travelers = getList(p);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(null, p, conn);
		}

		return travelers;
	}
}

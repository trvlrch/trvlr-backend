package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.Station;

import java.sql.*;

public class StationRepository extends Repository<Station> {

	private static StationRepository instance = new StationRepository();

	protected StationRepository() {
		super("station", new String[]{
				"id", "name", "weight"
		});
	}

	/**
	 * Get the current StationRepository instance
	 *
	 * @return StationRepository
	 */
	public static StationRepository getInstance() {
		return StationRepository.instance;
	}

	@Override
	protected Station convertToBusinessObject(ResultSet rs) throws SQLException {
		int id = rs.getInt(1);
		String name = rs.getString(2);
		int weight = rs.getInt(3);

		return new Station(id, name, weight);
	}

	@Override
	protected void prepareStatement(PreparedStatement statement, Station object) throws SQLException {
		statement.setString(1, object.getName());
		statement.setInt(2, object.getWeight());
	}

	/**
	 * Get a station by their name
	 *
	 * @param name String
	 * @return Station
	 */
	public Station getByName(String name) {
		Connection conn = null;
		PreparedStatement p = null;
		String sql = this.getQueryBuilder().generateSelectQuery(new String[]{"name"});
		Station station = null;

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql);
			p.setString(1, name);
			station = getSingle(p);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(null, p, conn);
		}

		return station;
	}
}

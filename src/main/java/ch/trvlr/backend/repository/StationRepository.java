package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.Station;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StationRepository extends Repository<Station> {

	private static StationRepository instance = new StationRepository();

	protected StationRepository() {
		super("station", new String[]{
				"id", "name"
		});
	}

	public static StationRepository getInstance() {
		return StationRepository.instance;
	}

	@Override
	protected Station convertToBusinessObject(ResultSet rs) throws SQLException {
		int id = rs.getInt(1);
		String name = rs.getString(2);

		return new Station(id, name);
	}

	@Override
	protected void prepareStatement(PreparedStatement statement, Station object) throws SQLException {
		statement.setString(1, object.getName());
	}

}

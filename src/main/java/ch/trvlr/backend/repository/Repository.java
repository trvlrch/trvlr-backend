package ch.trvlr.backend.repository;

import java.sql.*;
import java.util.ArrayList;

public abstract class Repository<T extends ISqlObject> {

	private String tableName;
	private String[] fields;
	private Connection dbConnection;

	protected Repository(String tableName, String[] fields) {
		this.tableName = tableName;
		this.fields = fields;

		// TODO refactor
		String dbname = System.getenv("DB_NAME");
		String user = System.getenv("DB_USER");
		String password = System.getenv("DB_PASSWORD");

		String connectionString = dbname + "?user=" + user;
		connectionString += (password != null) ? "&password=" + password : "";

		try {
			this.dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + connectionString);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	protected Connection getDbConnection() {
		return this.dbConnection;
	}

	protected String getTableTame() {
		return this.tableName;
	}

	protected String[] getFields() {
		return this.fields;
	}

	protected String getFieldsAsStringForSelect() {
		return this.getFieldsAsStringForSelectWithPrefix("");
	}

	protected String getFieldsAsStringForSelectWithPrefix(String prefix) {
		if (prefix.length() != 0) {
			prefix += ".";
		}

		String fieldsString = "";
		int i = 0;
		while (i < this.fields.length - 1) {
			fieldsString += prefix + "`" + fields[i] + "`, ";
			i++;
		}
		// no comma after last field
		return fieldsString + prefix + "`" + fields[i] + "` ";
	}

	protected String getFieldsAsStringForInsert() {
		String fieldsString = "";
		// we don't want the ID field for insert
		int i = 1;
		while (i < this.fields.length - 1) {
			fieldsString += "`" + fields[i] + "`, ";
			i++;
		}
		// no comma after last field
		return fieldsString + "`" + fields[i] + "` ";
	}

	protected String getValueStringForInsert() {
		String valueString = "?";
		for (int i = 1; i < this.fields.length - 1; i++) {
			valueString += ", ?";
		}
		return valueString;
	}

	protected String getFieldsAsStringForUpdate() {
		String fieldsString = "";
		// we don't want the ID field for update
		int i = 1;
		while (i < this.fields.length - 1) {
			fieldsString += "`" + fields[i] + "`=?, ";
			i++;
		}
		// no comma after last field
		return fieldsString + "`" + fields[i] + "`=? ";
	}

	protected abstract T convertToBusinessObject(ResultSet rs) throws SQLException;

	protected abstract void prepareStatement(PreparedStatement statement, T object) throws SQLException;

	public boolean add(T o) {
		String sql = "INSERT INTO " + this.getTableTame() +
				" (" + this.getFieldsAsStringForInsert() + ") " +
				"VALUES(" + this.getValueStringForInsert() + ")";

		try {
			PreparedStatement st = this.getDbConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			this.prepareStatement(st, o);
			// st.setInt(this.fields.length, o.getId());

			st.executeUpdate();
			if (st.getGeneratedKeys().next()) {
				System.out.println(st.getGeneratedKeys());
				return true;
			}
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}

		return false;
	}


	public boolean update(T o) {
		String sql = "UPDATE " + this.getTableTame() +
				" SET " + this.getFieldsAsStringForUpdate() +
				" WHERE " + this.getFields()[0] + " = ?";

		try {

			PreparedStatement st = this.getDbConnection().prepareStatement(sql);
			this.prepareStatement(st, o);
			st.setInt(this.fields.length, o.getId());
			st.executeUpdate();

			return true;

		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean save(T o) {
		if (o.getId() < 1)
			return this.add(o);
		else
			return this.update(o);
	}

	public T getById(int id) {
		String sql = "SELECT " + this.getFieldsAsStringForSelect() +
				" FROM " + this.getTableTame() +
				" WHERE " + this.getFields()[0] + " = ?";

		PreparedStatement p = prepareStatement(sql);

		try {
			p.setInt(1, id);
			return getSingle(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ArrayList<T> getAll() {
		ArrayList<T> result = new ArrayList<>();
		String sql = "SELECT " + this.getFieldsAsStringForSelect() +
				" FROM " + this.getTableTame();

		PreparedStatement p = prepareStatement(sql);
		return getList(p);
	}

	protected T getSingle(PreparedStatement p) {
		try {
			ResultSet rs = p.executeQuery();
			if (rs.next()) {
				return convertToBusinessObject(rs);
			}
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected ArrayList<T> getList(PreparedStatement p) {
		ArrayList<T> result = new ArrayList<>();

		try {
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				result.add(this.convertToBusinessObject(rs));
			}
			return result;
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	protected PreparedStatement prepareStatement(String sql) {
		try {
			return this.dbConnection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

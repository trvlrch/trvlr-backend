package ch.trvlr.backend.repository;

import java.sql.*;
import java.util.ArrayList;

public abstract class Repository<T extends ISqlObject> {

	private String tableName;
	private String[] fields;
	private Connection dbConnection;

	private QueryBuilder queryBuilder;

	protected Repository(String tableName, String[] fields) {
		this.tableName = tableName;
		this.fields = fields;
		this.queryBuilder = new QueryBuilder(tableName, fields);

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

	protected abstract T convertToBusinessObject(ResultSet rs) throws SQLException;

	protected abstract void prepareStatement(PreparedStatement statement, T object) throws SQLException;

	public int add(T o) {
		String sql = this.queryBuilder.generateInsertQuery();

		try {
			PreparedStatement st = this.getDbConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			this.prepareStatement(st, o);

			System.out.println(st.toString());

			st.executeUpdate();

			ResultSet keys = st.getGeneratedKeys();
			if (keys.next()) {
				return keys.getInt(1);
			}
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int update(T o) {
		String sql = this.queryBuilder.generateUpdateQuery();

		try {

			PreparedStatement st = this.getDbConnection().prepareStatement(sql);
			this.prepareStatement(st, o);
			st.setInt(this.fields.length, o.getId());
			st.executeUpdate();

			return o.getId();

		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int save(T o) {
		if (o.getId() < 1)
			return this.add(o);
		else
			return this.update(o);
	}

	public T getById(int id) {
		String sql = this.queryBuilder.generateSelectQuery(new String[] {"id"});


		try {
			PreparedStatement p = this.dbConnection.prepareStatement(sql);
			p.setInt(1, id);
			return getSingle(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ArrayList<T> getAll() {
		String sql = this.queryBuilder.generateSelectQuery();

		try {
			PreparedStatement p =  this.dbConnection.prepareStatement(sql);
			return getList(p);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
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

	protected Connection getDbConnection() {
		return this.dbConnection;
	}

	protected String getTableName() {
		return this.tableName;
	}

	protected String[] getFields() {
		return this.fields;
	}

	protected QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

}

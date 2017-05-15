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
		connectionString += "&useUnicode=true&characterEncoding=utf-8";

		try {
			this.dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + connectionString);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	protected abstract T convertToBusinessObject(ResultSet rs) throws SQLException;

	protected abstract void prepareStatement(PreparedStatement statement, T object) throws SQLException;

	/**
	 * Add a new object to the database
	 * <p>
	 * Returns the id of the newly created db entry or 0 if failure
	 *
	 * @param o T
	 * @return int
	 */
	public int add(T o) {
		String sql = this.queryBuilder.generateInsertQuery();

		try {
			PreparedStatement st = this.getDbConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			this.prepareStatement(st, o);

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

	/**
	 * Update an existing object in the database
	 * <p>
	 * Returns the id of the updated object or 0 if failure
	 *
	 * @param o T
	 * @return int
	 */
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

	/**
	 * Save an object in the database
	 * <p>
	 * The method decides if the object already exists or not based on the id and adds or updates
	 * the db entry.
	 * <p>
	 * Returns the id of the updated object or 0 if failure
	 *
	 * @param o T
	 * @return int
	 */
	public int save(T o) {
		if (o.getId() < 1)
			return this.add(o);
		else
			return this.update(o);
	}

	/**
	 * Get an object from the database by id
	 *
	 * @param id int
	 * @return T
	 */
	public T getById(int id) {
		String sql = this.queryBuilder.generateSelectQuery(new String[]{"id"});


		try {
			PreparedStatement p = this.dbConnection.prepareStatement(sql);
			p.setInt(1, id);
			return getSingle(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get all entries of a database table
	 *
	 * @return ArrayList<T>
	 */
	public ArrayList<T> getAll() {
		return this.getAll(null, 0);
	}

	/**
	 * Get all entries of a database table with an order by clause
	 *
	 * @return ArrayList<T>
	 */
	public ArrayList<T> getAll(String orderByClause) {
		return this.getAll(orderByClause, 0);
	}

	/**
	 * Get all entries of a database table with a limit
	 *
	 * @return ArrayList<T>
	 */
	public ArrayList<T> getAll(int limit) {
		return this.getAll(null, limit);
	}

	/**
	 * Get all entries of a database table with an order by clause and a limit
	 *
	 * @return ArrayList<T>
	 */
	public ArrayList<T> getAll(String orderByClause, int limit) {
		String sql = this.queryBuilder.generateSelectQuery(orderByClause, limit);

		try {
			PreparedStatement p = this.getDbConnection().prepareStatement(sql);
			return getList(p);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get all entries of a database table with an order by clause and a limit
	 *
	 * @return ArrayList<T>
	 */
	public ArrayList<T> getAll(String orderByClause, boolean asciiOnly) {
		String sql = this.queryBuilder.generateSelectQuery(orderByClause, 0);

		if (asciiOnly) {
			sql += " COLLATE ascii_general_ci";
		}

		try {
			PreparedStatement p = this.getDbConnection().prepareStatement(sql);
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

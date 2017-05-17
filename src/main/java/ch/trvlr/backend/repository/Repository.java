package ch.trvlr.backend.repository;

import java.sql.*;
import java.util.ArrayList;

public abstract class Repository<T extends ISqlObject> {

	private String tableName;
	private String[] fields;

	private QueryBuilder queryBuilder;

	private String connectionString;

	protected Repository(String tableName, String[] fields) {
		this.tableName = tableName;
		this.fields = fields;
		this.queryBuilder = new QueryBuilder(tableName, fields);

		// TODO refactor
		String dbname = System.getenv("DB_NAME");
		String user = System.getenv("DB_USER");
		String password = System.getenv("DB_PASSWORD");

		this.connectionString = dbname + "?user=" + user;
		this.connectionString += (password != null) ? "&password=" + password : "";
		this.connectionString += "&useUnicode=true&characterEncoding=utf-8";
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
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;
		String sql = this.queryBuilder.generateInsertQuery();
		int id = 0;

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			this.prepareStatement(p, o);

			p.executeUpdate();

			rs = p.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(rs, p, conn);
		}

		return id;
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
		Connection conn = null;
		PreparedStatement p = null;
		String sql = this.queryBuilder.generateUpdateQuery();
		int id = 0;

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql);
			this.prepareStatement(p, o);
			p.setInt(this.fields.length, o.getId());
			p.executeUpdate();

			id = o.getId();
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(null, p, conn);
		}

		return id;
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
		Connection conn = null;
		PreparedStatement p = null;
		String sql = this.queryBuilder.generateSelectQuery(new String[]{"id"});
		T t = null;

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql);
			p.setInt(1, id);
			t = getSingle(p);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, p, conn);
		}

		return t;
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
		Connection conn = null;
		ArrayList<T> entries = null;
		PreparedStatement p = null;
		String sql = this.queryBuilder.generateSelectQuery(orderByClause, limit);

		try {
			conn = this.getDbConnection();
			p = conn.prepareStatement(sql);
			entries = getList(p);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(null, p, conn);
		}

		return entries;
	}

	protected T getSingle(PreparedStatement p) {
		ResultSet rs = null;
		T t = null;

		try {
			rs = p.executeQuery();
			if (rs.next()) {
				t = convertToBusinessObject(rs);
			}
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, null, null);
		}

		return t;
	}

	protected ArrayList<T> getList(PreparedStatement p) {
		ResultSet rs = null;
		ArrayList<T> result = new ArrayList<>();

		try {
			rs = p.executeQuery();
			while (rs.next()) {
				result.add(this.convertToBusinessObject(rs));
			}
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(rs, null, null);
		}

		return result;
	}

	protected Connection getDbConnection() {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + connectionString);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return conn;
	}

	protected void closeConnection(ResultSet rs, PreparedStatement p, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) { /* ignored */}
		}
		if (p != null) {
			try {
				p.close();
			} catch (SQLException e) { /* ignored */}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) { /* ignored */}
		}
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

package ch.trvlr.backend.repository;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class QueryBuilder {

	private String[] fields;

	private String tableName;

	/**
	 * Constructor for QueryBuilder
	 *
	 * @param tableName String
	 * @param fields    String[]
	 */
	public QueryBuilder(String tableName, String[] fields) {
		this.tableName = tableName;
		this.fields = fields;
	}

	/**
	 * Generate an insert query
	 *
	 * @return String
	 */
	public String generateInsertQuery() {
		return generateInsertQuery(this.getTableTame(), this.getFields(), 1);
	}

	/**
	 * Generate an insert query
	 *
	 * @param tableName String
	 * @param fields    String[]
	 * @return
	 */
	public String generateInsertQuery(String tableName, String[] fields) {
		return generateInsertQuery(tableName, fields, 0);
	}

	/**
	 * Generate an insert query
	 *
	 * @param tableName String
	 * @param fields    String[]
	 * @param offset    int
	 * @return
	 */
	private String generateInsertQuery(String tableName, String[] fields, int offset) {
		return "INSERT INTO " + tableName +
				" (" + this.getFieldsAsStringForInsert(fields, offset) + ") " +
				"VALUES(" + this.getValueStringForInsert(fields, offset) + ")";
	}

	/**
	 * Generate an update query
	 *
	 * @return String
	 */
	public String generateUpdateQuery() {
		return generateUpdateQuery(this.getTableTame(), this.getFields(), 1);
	}

	/**
	 * Generate an update query
	 *
	 * @param tableName String
	 * @param fields    String[]
	 * @return String
	 */
	public String generateUpdateQuery(String tableName, String[] fields) {
		return generateUpdateQuery(tableName, fields, 0);
	}

	/**
	 * Generate an update query
	 *
	 * @param tableName String
	 * @param fields    String[]
	 * @param offset    int
	 * @return String
	 */
	private String generateUpdateQuery(String tableName, String[] fields, int offset) {
		return "UPDATE " + tableName +
				" SET " + this.getFieldsAsStringForUpdate(fields, offset) +
				" WHERE " + fields[0] + " = ?";
	}

	/**
	 * Generate a select query
	 *
	 * @return String
	 */
	public String generateSelectQuery() {
		return generateSelectQuery(null, 0);
	}

	/**
	 * Generate a select query
	 *
	 * @param orderBy String
	 * @param limit   int
	 * @return String
	 */
	public String generateSelectQuery(String orderBy, int limit) {
		String sql = "SELECT " + this.getFieldsAsStringForSelect() +
				" FROM " + this.getTableTame();


		if (orderBy != null && orderBy.length() > 0) {
			sql += " ORDER BY " + orderBy;
		}

		if (limit > 0) {
			sql += " LIMIT " + limit;
		}

		return sql;
	}

	/**
	 * Generate a select query
	 *
	 * @param whereFields String[]
	 * @return String
	 */
	public String generateSelectQuery(String[] whereFields) {
		return "SELECT " + this.getFieldsAsStringForSelect() +
				" FROM " + this.getTableTame() + this.getWhere(whereFields);
	}

	/**
	 * Generate a delete query
	 *
	 * @param whereFields String[]
	 * @return
	 */
	public String generateDeleteQuery(String[] whereFields) {
		return generateDeleteQuery(this.getTableTame(), whereFields);
	}

	/**
	 * Generate a delete query
	 *
	 * @param tableName   String
	 * @param whereFields String[]
	 * @return String
	 */
	public String generateDeleteQuery(String tableName, String[] whereFields) {
		return "DELETE FROM " + tableName + this.getWhere(whereFields);
	}

	/**
	 * Get all fields as string for a select query
	 *
	 * @return String
	 */
	public String getFieldsAsStringForSelect() {
		return this.getFieldsAsStringForSelectWithPrefix("");
	}

	/**
	 * Get all fields as string for a select query with a table prefix
	 *
	 * @param prefix String
	 * @return String
	 */
	public String getFieldsAsStringForSelectWithPrefix(String prefix) {
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

	private String getFieldsAsStringForInsert(String[] fields, int offset) {
		String fieldsString = "";
		// we don't want the ID field for insert
		int i = offset;
		while (i < fields.length - 1) {
			fieldsString += "`" + fields[i] + "`, ";
			i++;
		}
		// no comma after last field
		return fieldsString + "`" + fields[i] + "` ";
	}

	private String getValueStringForInsert(String[] fields, int offset) {
		String valueString = "?";
		for (int i = offset; i < fields.length - 1; i++) {
			valueString += ", ?";
		}
		return valueString;
	}

	private String getFieldsAsStringForUpdate(String[] fields, int offset) {
		String fieldsString = "";
		// we don't want the ID field for update
		int i = offset;
		while (i < fields.length - 1) {
			fieldsString += "`" + fields[i] + "`=?, ";
			i++;
		}
		// no comma after last field
		return fieldsString + "`" + fields[i] + "`=? ";
	}

	private String getWhere(String[] whereFields) {
		String where = " WHERE ";

		int length = whereFields.length;
		for (int i = 0; i < length; i++) {
			where += whereFields[i] + " = ? " + (i < length - 1 ? ", " : " ");
		}
		return where;
	}

	private String getTableTame() {
		return this.tableName;
	}

	private String[] getFields() {
		return this.fields;
	}
}

package ch.trvlr.backend.repository;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class QueryBuilder {

	private String[] fields;

	private String tableName;

	public QueryBuilder(String tableName, String[] fields) {
		this.tableName = tableName;
		this.fields = fields;
	}

	public String generateInsertQuery() {
		return generateInsertQuery(this.getTableTame(), this.getFields(), 1);
	}

	public String generateInsertQuery(String tableName, String[] fields) {
		return generateInsertQuery(tableName, fields, 0);
	}

	private String generateInsertQuery(String tableName, String[] fields, int offset) {
		return "INSERT INTO " + tableName +
				" (" + this.getFieldsAsStringForInsert(fields, offset) + ") " +
				"VALUES(" + this.getValueStringForInsert(fields, offset) + ")";
	}

	public String generateUpdateQuery() {
		return generateUpdateQuery(this.getTableTame(), this.getFields(), 1);
	}

	public String generateUpdateQuery(String tableName, String[] fields) {
		return generateUpdateQuery(tableName, fields, 0);
	}

	private String generateUpdateQuery(String tableName, String[] fields, int offset) {
		return "UPDATE " + tableName +
				" SET " + this.getFieldsAsStringForUpdate(fields, offset) +
				" WHERE " + fields[0] + " = ?";
	}

	public String generateSelectQuery() {
		return generateSelectQuery(null, 0);
	}

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

	public String generateSelectQuery(String[] whereFields) {
		return "SELECT " + this.getFieldsAsStringForSelect() +
				" FROM " + this.getTableTame() + this.getWhere(whereFields);
	}

	public String generateDeleteQuery(String[] whereFields) {
		return generateDeleteQuery(this.getTableTame(), whereFields);
	}

	public String generateDeleteQuery(String tableName, String[] whereFields) {
		return "DELETE FROM " + tableName + this.getWhere(whereFields);
	}

	public String getFieldsAsStringForSelect() {
		return this.getFieldsAsStringForSelectWithPrefix("");
	}

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

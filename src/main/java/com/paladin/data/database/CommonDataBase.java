package com.paladin.data.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.paladin.data.database.JDBCHelper.QueryHandler;
import com.paladin.data.database.model.Column;
import com.paladin.data.database.model.DataBase;
import com.paladin.data.database.model.Table;
import com.paladin.data.database.model.constraint.ColumnConstraint;
import com.paladin.data.database.model.constraint.ConstraintType;
import com.paladin.data.database.model.constraint.TableConstraint;

/**
 * 实现了通用的方法的数据库源
 * 
 * @author TontZhou
 * 
 */
public abstract class CommonDataBase extends DataBaseSource {

	public CommonDataBase(DataBaseConfig config) {
		super(config);
	}

	protected DataBase dataBase;

	private boolean refreshed = false;

	/**
	 * 获取数据库所有表
	 * 
	 * @return
	 */
	public DataBase getDataBase(boolean refresh) {
		if (refresh || !refreshed)
			refreshTables();
		return dataBase;

	}

	private DataBase queryTableFromOracle() throws SQLException {

		String sql = "SELECT A.*,B.COMMENTS FROM USER_TAB_COLS A LEFT JOIN USER_COL_COMMENTS B ON B.TABLE_NAME = A.TABLE_NAME AND B.COLUMN_NAME = A.COLUMN_NAME ORDER BY A.TABLE_NAME,COLUMN_ID";

		final DataBase dataBase = JDBCHelper.queryList(sql, getRealDataSource(), new QueryHandler<DataBase>() {

			@Override
			public DataBase handle(ResultSet resultSet) throws SQLException {

				DataBase dataBase = new DataBase(getDataBaseConfig().getName());

				while (resultSet.next()) {
					String columnName = resultSet.getString("COLUMN_NAME");
					String dataType = resultSet.getString("DATA_TYPE");
					Object dataLength = resultSet.getObject("DATA_LENGTH");
					Object dataPrecision = resultSet.getObject("DATA_PRECISION");
					Object dataScale = resultSet.getObject("DATA_SCALE");
					String nullable = resultSet.getString("NULLABLE");
					String defaultValue = resultSet.getString("DATA_DEFAULT");
					int orderIndex = resultSet.getInt("COLUMN_ID");
					String tableName = resultSet.getString("TABLE_NAME");
					String comment = resultSet.getString("COMMENTS");

					Column column = new Column();
					column.setName(columnName);
					column.setDataType(dataType);
					column.setDataLength(toInteger(dataLength));
					column.setDataPrecision(toInteger(dataPrecision));
					column.setDataScale(toInteger(dataScale));
					column.setNullable("Y".equals(nullable));
					column.setDefaultValue(defaultValue);
					column.setOrderIndex(orderIndex);
					column.setComment(comment);

					Table table = dataBase.getChild(tableName);
					if (table == null) {
						table = new Table(tableName);
						dataBase.addChild(tableName, table);
					}
					table.addChild(columnName, column);
				}

				return dataBase;
			}

		});

		// 查找主键
		String priSql = "select A.CONSTRAINT_NAME,A.CONSTRAINT_TYPE,B.TABLE_NAME,B.COLUMN_NAME,D.TABLE_NAME AS R_TABLE_NAME,D.COLUMN_NAME AS R_COLUMN_NAME from USER_CONSTRAINTS A "
				+ "LEFT JOIN USER_CONS_COLUMNS B ON B.CONSTRAINT_NAME = A.CONSTRAINT_NAME "
				+ "LEFT JOIN USER_CONSTRAINTS C ON A.R_CONSTRAINT_NAME = C.CONSTRAINT_NAME "
				+ "LEFT JOIN USER_CONS_COLUMNS D ON D.CONSTRAINT_NAME = C.CONSTRAINT_NAME "
				+ "WHERE A.CONSTRAINT_TYPE = 'R' OR A.CONSTRAINT_TYPE = 'U' OR A.CONSTRAINT_TYPE = 'P'" + "ORDER BY A.CONSTRAINT_NAME";

		JDBCHelper.queryList(priSql, getRealDataSource(), new QueryHandler<DataBase>() {

			@Override
			public DataBase handle(ResultSet resultSet) throws SQLException {

				String previousName = null;
				TableConstraint tableConstraint = null;

				while (resultSet.next()) {
					String name = resultSet.getString("CONSTRAINT_NAME");
					String tableName = resultSet.getString("TABLE_NAME");
					String columnName = resultSet.getString("COLUMN_NAME");
					String referencedTableName = resultSet.getString("R_TABLE_NAME");
					String referencedColumnName = resultSet.getString("R_COLUMN_NAME");
					String constraintType = resultSet.getString("CONSTRAINT_TYPE");

					ConstraintType type = null;
					switch (constraintType) {
					case "U":
						type = ConstraintType.UNIQUE;
						break;
					case "P":
						type = ConstraintType.PRIMARY;
						break;
					case "R":
						type = ConstraintType.REFERENCE;
						break;
					}

					if (!name.equals(previousName)) {
						tableConstraint = new TableConstraint(name, tableName, type);
						Table table = dataBase.getChild(tableName);
						if (table == null)
							continue;
						table.addTableConstraint(tableConstraint);
						previousName = name;
					}

					ColumnConstraint columnConstraint;

					if (type == ConstraintType.REFERENCE) {
						columnConstraint = tableConstraint.addRefrencedConstraint(tableName, columnName, referencedTableName, referencedColumnName);
					} else {
						columnConstraint = tableConstraint.addConstraint(tableName, columnName);
					}

					Column column = dataBase.getChild(tableName).getChild(columnName);

					if (type == ConstraintType.UNIQUE)
						column.setUniqueKey(columnConstraint);
					else if (type == ConstraintType.PRIMARY)
						column.setPrimaryKey(columnConstraint);
					else if (type == ConstraintType.REFERENCE)
						column.setForeignKey(columnConstraint);

				}
				return null;
			}

		});

		return dataBase;

	}

	private DataBase queryTableFromMySql(final String dbname) throws SQLException {

		String sql = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='" + dbname + "'  ORDER BY TABLE_NAME,ORDINAL_POSITION";

		final DataBase dataBase = JDBCHelper.queryList(sql, getRealDataSource(), new QueryHandler<DataBase>() {

			@Override
			public DataBase handle(ResultSet resultSet) throws SQLException {

				DataBase dataBase = new DataBase(dbname);

				while (resultSet.next()) {
					String columnName = resultSet.getString("COLUMN_NAME");
					String dataType = resultSet.getString("DATA_TYPE");
					Object dataLength = resultSet.getObject("CHARACTER_MAXIMUM_LENGTH");
					Object dataPrecision = resultSet.getObject("NUMERIC_PRECISION");
					Object dataScale = resultSet.getObject("NUMERIC_SCALE");
					String nullable = resultSet.getString("IS_NULLABLE");
					String defaultValue = resultSet.getString("COLUMN_DEFAULT");
					int orderIndex = resultSet.getInt("ORDINAL_POSITION");
					String tableName = resultSet.getString("TABLE_NAME");
					String comment = resultSet.getString("COLUMN_COMMENT");
					String extra = resultSet.getString("EXTRA");
					String unsigned = resultSet.getString("COLUMN_TYPE");

					Column column = new Column();
					column.setName(columnName);
					column.setDataType(dataType);
					column.setDataLength(toInteger(dataLength));
					column.setDataPrecision(toInteger(dataPrecision));
					column.setDataScale(toInteger(dataScale));
					column.setNullable("YES".equals(nullable));
					column.setDefaultValue(defaultValue);
					column.setOrderIndex(orderIndex);
					column.setComment(comment);
					column.setUnsigned(unsigned.contains("unsigned"));

					if (extra != null) {
						String[] ss = extra.split(";");
						for (String s : ss)
							if ("auto_increment".equals(s))
								column.setAutoIncrement(true);
					}

					Table table = dataBase.getChild(tableName);
					if (table == null) {
						table = new Table(tableName);
						dataBase.addChild(tableName, table);
					}
					table.addChild(columnName, column);
				}

				return dataBase;
			}

		});

		sql = "SELECT A.CONSTRAINT_NAME,A.TABLE_NAME,A.COLUMN_NAME,A.REFERENCED_TABLE_NAME,A.REFERENCED_COLUMN_NAME,B.CONSTRAINT_TYPE "
				+ "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE A "
				+ "LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS B ON A.CONSTRAINT_NAME = B.CONSTRAINT_NAME AND B.TABLE_SCHEMA = A.TABLE_SCHEMA AND A.TABLE_NAME = B.TABLE_NAME "
				+ "WHERE A.TABLE_SCHEMA = '" + dbname + "' AND (A.REFERENCED_TABLE_SCHEMA IS NULL OR A.REFERENCED_TABLE_SCHEMA = '" + dbname
				+ "') ORDER BY A.CONSTRAINT_NAME,A.TABLE_NAME";

		JDBCHelper.queryList(sql, getRealDataSource(), new QueryHandler<DataBase>() {

			@Override
			public DataBase handle(ResultSet resultSet) throws SQLException {

				String previousName = null;
				String previousTable = null;
				TableConstraint tableConstraint = null;

				while (resultSet.next()) {
					String name = resultSet.getString("CONSTRAINT_NAME");
					String tableName = resultSet.getString("TABLE_NAME");
					String columnName = resultSet.getString("COLUMN_NAME");
					String referencedTableName = resultSet.getString("REFERENCED_TABLE_NAME");
					String referencedColumnName = resultSet.getString("REFERENCED_COLUMN_NAME");
					String constraintType = resultSet.getString("CONSTRAINT_TYPE");

					ConstraintType type = null;
					switch (constraintType) {
					case "UNIQUE":
						type = ConstraintType.UNIQUE;
						break;
					case "PRIMARY KEY":
						type = ConstraintType.PRIMARY;
						break;
					case "FOREIGN KEY":
						type = ConstraintType.REFERENCE;
						break;
					}

					if (!name.equals(previousName) || !tableName.equals(previousTable)) {
						tableConstraint = new TableConstraint(name, tableName, type);
						Table table = dataBase.getChild(tableName);
						if (table == null)
							continue;
						table.addTableConstraint(tableConstraint);
						previousName = name;
						previousTable = tableName;
					}

					ColumnConstraint columnConstraint;

					if (type == ConstraintType.REFERENCE) {
						columnConstraint = tableConstraint.addRefrencedConstraint(tableName, columnName, referencedTableName, referencedColumnName);
					} else {
						columnConstraint = tableConstraint.addConstraint(tableName, columnName);
					}

					Column column = dataBase.getChild(tableName).getChild(columnName);

					if (type == ConstraintType.UNIQUE)
						column.setUniqueKey(columnConstraint);
					else if (type == ConstraintType.PRIMARY)
						column.setPrimaryKey(columnConstraint);
					else if (type == ConstraintType.REFERENCE)
						column.setForeignKey(columnConstraint);

				}

				return dataBase;
			}

		});

		return dataBase;
	}

	private Integer toInteger(Object number) {
		if (number instanceof Number) {
			return ((Number) number).intValue();
		}

		return null;
	}

	/**
	 * 刷新数据库中表
	 */
	private void refreshTables() {

		DataBaseType type = config.getType();

		try {

			if (type == DataBaseType.ORACLE) {
				dataBase = queryTableFromOracle();
			} else if (type == DataBaseType.MYSQL) {

				String url = config.getUrl();

				int i = url.indexOf('?');
				String s = url.substring(0, i);
				if (i > 0)
					s = s.substring(s.lastIndexOf('/') + 1, i);
				else
					s = s.substring(s.lastIndexOf('/') + 1);

				dataBase = queryTableFromMySql(s);
			} else {
				// do not
				throw new RuntimeException("Not support DataBase Type = [" + type + "] yet");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshed = true;
		}

	}

	private static final Map<DataBaseType, String> testSqlMap;

	static {
		testSqlMap = new HashMap<>();
		testSqlMap.put(DataBaseType.MYSQL, "select 1");
		testSqlMap.put(DataBaseType.SQLSERVER, "select 1");
		testSqlMap.put(DataBaseType.ORACLE, "select 1 from dual");

	}

	public boolean testConnect() {

		String sql = testSqlMap.get(config.getType());

		try {
			Boolean result = JDBCHelper.queryList(sql, getRealDataSource(), new QueryHandler<Boolean>() {

				@Override
				public Boolean handle(ResultSet resultSet) throws SQLException {
					return resultSet.next();
				}

			});

			return result;

		} catch (SQLException e) {

		}

		return false;

	}

}

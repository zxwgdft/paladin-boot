package com.paladin.data.model.build;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class DbBuildTable {

	public final static String COLUMN_FIELD_CONNECTION_NAME = "connectionName";
	public final static String COLUMN_FIELD_TABLE_NAME = "tableName";
	
	// 
	@Id
	@GeneratedValue(generator = "UUID")
	private String id;

	// 
	private String connectionName;

	// 
	private String tableName;

	// 
	private String tableTitle;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

}
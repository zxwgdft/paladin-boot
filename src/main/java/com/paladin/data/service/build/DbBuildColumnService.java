package com.paladin.data.service.build;

import com.paladin.data.model.build.DbBuildColumn;
import com.paladin.framework.service.Condition;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbBuildColumnService extends ServiceSupport<DbBuildColumn> {

	public List<DbBuildColumn> getDbBuildColumn(String connectionName, String tableName) {
		return searchAll(new Condition(DbBuildColumn.COLUMN_FIELD_CONNECTION_NAME, QueryType.EQUAL, connectionName),
				new Condition(DbBuildColumn.COLUMN_FIELD_TABLE_NAME, QueryType.EQUAL, tableName));

	}

	public int removeByTable(String dbName, String tableName) {
		return removeByCondition(new Condition(DbBuildColumn.COLUMN_FIELD_CONNECTION_NAME, QueryType.EQUAL, dbName),
				new Condition(DbBuildColumn.COLUMN_FIELD_CONNECTION_NAME, QueryType.EQUAL, tableName));
	}

}
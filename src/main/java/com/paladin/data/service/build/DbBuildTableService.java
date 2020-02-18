package com.paladin.data.service.build;

import java.util.List;

import org.springframework.stereotype.Service;

import com.paladin.data.model.build.DbBuildTable;
import com.paladin.framework.common.Condition;
import com.paladin.framework.common.QueryType;
import com.paladin.framework.core.ServiceSupport;

@Service
public class DbBuildTableService extends ServiceSupport<DbBuildTable> {

	public DbBuildTable getDbBuildColumn(String connectionName, String tableName) {
		List<DbBuildTable> result = searchAll(new Condition(DbBuildTable.COLUMN_FIELD_CONNECTION_NAME, QueryType.EQUAL, connectionName),
				new Condition(DbBuildTable.COLUMN_FIELD_CONNECTION_NAME, QueryType.EQUAL, tableName));
		if (result != null && result.size() > 0) {
			return result.get(0);
		}

		return null;
	}

	public int removeByTable(String dbName, String tableName) {
		return removeByCondition(new Condition(DbBuildTable.COLUMN_FIELD_CONNECTION_NAME, QueryType.EQUAL, dbName),
				new Condition(DbBuildTable.COLUMN_FIELD_CONNECTION_NAME, QueryType.EQUAL, tableName));
	}

}
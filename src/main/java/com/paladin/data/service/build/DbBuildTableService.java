package com.paladin.data.service.build;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paladin.data.mapper.build.DbBuildTableMapper;
import com.paladin.data.model.build.DbBuildTable;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbBuildTableService extends ServiceSupport<DbBuildTable, DbBuildTableMapper> {

    public DbBuildTable getDbBuildColumn(String connectionName, String tableName) {
        List<DbBuildTable> result = findList(new LambdaQueryWrapper<DbBuildTable>()
                .eq(DbBuildTable::getConnectionName, connectionName)
                .eq(DbBuildTable::getTableName, tableName)
        );

        if (result != null && result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

    public void removeByTable(String dbName, String tableName) {
        getSqlMapper().removeTable(dbName, tableName);
    }

}
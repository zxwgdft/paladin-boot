package com.paladin.data.service.build;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paladin.data.mapper.build.DbBuildColumnMapper;
import com.paladin.data.model.build.DbBuildColumn;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbBuildColumnService extends ServiceSupport<DbBuildColumn, DbBuildColumnMapper> {

    public List<DbBuildColumn> getDbBuildColumn(String connectionName, String tableName) {
        return findList(new LambdaQueryWrapper<DbBuildColumn>()
                .eq(DbBuildColumn::getConnectionName, connectionName)
                .eq(DbBuildColumn::getTableName, tableName)
        );
    }

    public void removeByTable(String dbName, String tableName) {
        getSqlMapper().removeColumns(dbName, tableName);
    }

}
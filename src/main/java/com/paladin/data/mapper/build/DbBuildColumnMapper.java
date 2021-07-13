package com.paladin.data.mapper.build;

import com.paladin.data.model.build.DbBuildColumn;
import com.paladin.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface DbBuildColumnMapper extends CommonMapper<DbBuildColumn> {
    @Delete("DELETE FROM db_build_column WHERE connection_name = #{dbName} AND table_name = #{tableName}")
    int removeColumns(@Param("dbName") String dbName, @Param("tableName") String tableName);
}
package com.paladin.data.mapper.build;

import com.paladin.data.model.build.DbBuildTable;
import com.paladin.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface DbBuildTableMapper extends CommonMapper<DbBuildTable> {
    @Delete("DELETE FROM db_build_table WHERE connection_name = #{dbName} AND table_name = #{tableName}")
    int removeTable(@Param("dbName") String dbName, @Param("tableName") String tableName);
}
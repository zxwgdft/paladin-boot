package com.paladin.data.model.build;

import com.paladin.framework.core.configuration.mybatis.GenIdImpl;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;

@Getter
@Setter
public class DbBuildTable {

    public final static String COLUMN_FIELD_CONNECTION_NAME = "connectionName";
    public final static String COLUMN_FIELD_TABLE_NAME = "tableName";

    //
    @Id
    @KeySql(genId = GenIdImpl.class)
    private String id;

    //
    private String connectionName;

    //
    private String tableName;

    //
    private String tableTitle;


}
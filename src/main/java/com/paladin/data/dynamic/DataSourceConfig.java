package com.paladin.data.dynamic;

import com.paladin.data.dynamic.DataSourceFacade.DataSourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSourceConfig {

    /**
     * 数据库名称，唯一
     */
    private String name;

    /**
     * 数据链接URL
     */
    private String url;

    /**
     * 用户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否启用
     */
    private boolean enabled = true;


    private DataSourceType type = DataSourceType.HIKARI;

    private boolean autoCommit = true;

    private String connectionTestQuery;

    // ------------- hikari ---------------

    private int maximumPoolSize = 5;

    private int minimumIdle = 5;

    private int idleTimeout = 600000;

    private int maxLifetime = 1800000;

    private int connectionTimeout = 30000;

    // ------------- druid ---------------

    private int maxActive = 15;

    private int initialSize = 5;

    private int minIdle = 5;

    private int maxWait = 30000;

    private boolean testOnBorrow = false;

    private boolean testOnReturn = false;

    private boolean testWhileIdle = true;

    private int timeBetweenEvictionRunsMillis = 600000;

    private int minEvictableIdleTimeMillis = 300000;

    private Boolean poolPreparedStatements;        // oracle 建议为true

    private int maxPoolPreparedStatementPerConnectionSize = -1;

}

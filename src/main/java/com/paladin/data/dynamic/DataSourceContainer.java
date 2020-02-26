package com.paladin.data.dynamic;

import com.paladin.framework.service.VersionContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "paladin", value = "dynamic-datasource-enabled", havingValue = "true", matchIfMissing = false)
public class DataSourceContainer implements VersionContainer {
    
    private static String staticLocalSourceName;
    private static DataSource staticLocalDataSource;

    @Resource
    private DynamicDataSourceProperties properties;

    @Autowired(required = false)
    private DataSource localDataSource;

    private static Map<String, DataSourceFacade> dsMap = new HashMap<>();

    public void initialize() {
        staticLocalSourceName = properties.getLocalSourceName();
        staticLocalDataSource = localDataSource;

        List<DataSourceConfig> sources = properties.getSource();
        if (sources != null) {
            for (DataSourceConfig source : sources) {
                dsMap.put(source.getName(), new DataSourceFacade(source));
            }
        }

        log.info("多数据源容器初始化完毕，共包含数据源" + dsMap.size() + "个");
    }

    public static DataSource getRealDataSource(String name) {
        if (staticLocalDataSource != null && staticLocalSourceName.equals(name)) {
            return staticLocalDataSource;
        }

        DataSourceFacade facade = dsMap.get(name);
        return facade == null ? null : facade.getRealDataSource();
    }

    @Override
    public boolean versionChangedHandle(long version) {
        initialize();
        return false;
    }

    @Override
    public String getId() {
        return "data_source_container";
    }
}

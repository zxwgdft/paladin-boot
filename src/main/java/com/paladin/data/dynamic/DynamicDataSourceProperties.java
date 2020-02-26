package com.paladin.data.dynamic;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "paladin.dynamic")
@ConditionalOnProperty(prefix = "paladin", value = "dynamic-datasource-enabled", havingValue = "true", matchIfMissing = false)
public class DynamicDataSourceProperties {

    private String localSourceName = "local";

    private List<DataSourceConfig> source;

}

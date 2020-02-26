package com.paladin.data.dynamic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "paladin.dynamic.mybatis")
@ConditionalOnProperty(prefix = "paladin", value = "dynamic-datasource-enabled", havingValue = "true", matchIfMissing = false)
public class SqlSessionFactoryProperties {

    private String typeAliasesPackage;
    private String mapperLocation;
    private boolean pageEnabled = true;

}

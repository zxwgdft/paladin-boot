package com.paladin.data.dynamic;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "paladin.dynamic")
public class DynamicDataSourceProperties {

	private String localSourceName = "local";

	private List<DataSourceConfig> source;

	public List<DataSourceConfig> getSource() {
		return source;
	}

	public void setSource(List<DataSourceConfig> source) {
		this.source = source;
	}

	public String getLocalSourceName() {
		return localSourceName;
	}

	public void setLocalSourceName(String localSourceName) {
		this.localSourceName = localSourceName;
	}

}

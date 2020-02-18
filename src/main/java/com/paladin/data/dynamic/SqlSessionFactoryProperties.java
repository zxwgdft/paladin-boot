package com.paladin.data.dynamic;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "paladin.dynamic.mybatis")
public class SqlSessionFactoryProperties {

	private String typeAliasesPackage;
	private String mapperLocation;
	private boolean pageEnabled = true;
	
	public String getTypeAliasesPackage() {
		return typeAliasesPackage;
	}

	public void setTypeAliasesPackage(String typeAliasesPackage) {
		this.typeAliasesPackage = typeAliasesPackage;
	}

	public String getMapperLocation() {
		return mapperLocation;
	}

	public void setMapperLocation(String mapperLocation) {
		this.mapperLocation = mapperLocation;
	}

	public boolean isPageEnabled() {
		return pageEnabled;
	}

	public void setPageEnabled(boolean pageEnabled) {
		this.pageEnabled = pageEnabled;
	}

}

package com.paladin.framework.core.configuration.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.paladin.framework.core.GlobalProperties;

@ConfigurationProperties(prefix = "paladin.mybatis")
public class MyBatisProperties {

	private String typeAliasesPackage = "com.paladin.common.model,com.paladin.data.model,com.paladin." + GlobalProperties.project + ".model";
	// 由于无法加载顺序问题mapperPackage数值用Environment读取
	private String mapperPackage;
	private String mapperLocation = "classpath:mapper/**/*.xml";
	
	private boolean pageEnabled = true;

	public String getTypeAliasesPackage() {
		return typeAliasesPackage;
	}

	public void setTypeAliasesPackage(String typeAliasesPackage) {
		this.typeAliasesPackage = typeAliasesPackage;
	}

	public String getMapperPackage() {
		return mapperPackage;
	}

	public void setMapperPackage(String mapperPackage) {
		this.mapperPackage = mapperPackage;
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

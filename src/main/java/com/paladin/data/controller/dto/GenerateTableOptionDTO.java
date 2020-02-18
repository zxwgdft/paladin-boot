package com.paladin.data.controller.dto;

import java.util.List;

import com.paladin.data.model.build.DbBuildColumn;

/**
 * @see com.paladin.data.generate.GenerateTableOption
 * @author TontoZhou
 * @since 2018年4月11日
 */
public class GenerateTableOptionDTO {

	private String dbName;

	private String tableName;

	private String title;

	private String basePackage;

	private String model;

	private String subModel;

	private String projectPath;
	
	private String filePath;

	private List<DbBuildColumn> columnBuildOptions;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getSubModel() {
		return subModel;
	}

	public void setSubModel(String subModel) {
		this.subModel = subModel;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<DbBuildColumn> getColumnBuildOptions() {
		return columnBuildOptions;
	}

	public void setColumnBuildOptions(List<DbBuildColumn> columnBuildOptions) {
		this.columnBuildOptions = columnBuildOptions;
	}

}

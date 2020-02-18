package com.paladin.data.generate;

import com.paladin.data.database.DataBaseType;
import com.paladin.data.database.DataTypeUtil;
import com.paladin.data.database.model.Column;
import com.paladin.data.model.build.DbBuildColumn;
import com.paladin.framework.utils.reflect.NameUtil;

public class GenerateColumnOption {

	/**
	 * 具体列描述
	 */
	private Column column;

	/**
	 * 数据库类型
	 */
	private DataBaseType dataBaseType;

	/**
	 * 是否主键
	 */
	private boolean isPrimary;

	/**
	 * 自动生成方式，UUID等
	 */
	private String generatedType;

	/**
	 * 属性java类型
	 */
	private Class<?> fieldType;

	/**
	 * 属性名称
	 */
	private String fieldName;

	/**
	 * 构建列配置
	 */
	private DbBuildColumn buildColumnOption;

	public GenerateColumnOption(Column column, DataBaseType dataBaseType) {
		this.column = column;
		this.dataBaseType = dataBaseType;
		this.isPrimary = column.isPrimary();

		this.fieldType = DataTypeUtil.getJavaType(column, dataBaseType);
		this.fieldName = NameUtil.underline2hump(column.getName());		
	}

	public Column getColumn() {
		return column;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public DataBaseType getDataBaseType() {
		return dataBaseType;
	}

	public Class<?> getFieldType() {
		return fieldType;
	}

	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public String getGeneratedType() {
		return generatedType;
	}

	public void setGeneratedType(String generatedType) {
		this.generatedType = generatedType;
	}

	public DbBuildColumn getBuildColumnOption() {
		return buildColumnOption;
	}

	public void setBuildColumnOption(DbBuildColumn buildColumnOption) {
		this.buildColumnOption = buildColumnOption;
	}
	
}

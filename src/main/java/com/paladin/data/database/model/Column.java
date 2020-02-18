package com.paladin.data.database.model;

import com.paladin.data.database.model.constraint.ColumnConstraint;
import com.paladin.data.database.model.constraint.ConstraintMode;

public class Column {

	/**
	 * 列名
	 */
	private String name;

	/**
	 * 数据类型
	 */
	private String dataType;

	/**
	 * 数据长度
	 */
	private Integer dataLength;

	/**
	 * 数据精度
	 */
	private Integer dataPrecision;

	/**
	 * 数据规模
	 */
	private Integer dataScale;

	/**
	 * 是否能为NULL
	 */
	private boolean nullable;

	/**
	 * 默认值
	 */
	private String defaultValue;

	/**
	 * 注释
	 */
	private String comment;

	/**
	 * 排序号
	 */
	private Integer orderIndex;
	
	/**
	 * 有无符号（用于MYSQL）
	 */
	private boolean unsigned;

	// --------------- 约束 ------------------

	/*
	 * 
	 * 如果有交集的组合唯一键或外键，则会出现一列由多个ColumnConstraint，
	 * 这种情况会出现被替换的问题，但是这里不做这种特殊情况处理
	 * 
	 */
	
	/**
	 * 是否主键
	 */
	private ColumnConstraint primaryKey;

	/**
	 * 是否唯一
	 */
	private ColumnConstraint uniqueKey;

	/**
	 * 外键 ,只查找关联本数据库的外键
	 */
	private ColumnConstraint foreignKey;

	/**
	 * 是否自增 NULL为不确定
	 */
	private Boolean autoIncrement;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getDataLength() {
		return dataLength;
	}

	public void setDataLength(Integer dataLength) {
		this.dataLength = dataLength;
	}

	public Integer getDataPrecision() {
		return dataPrecision;
	}

	public void setDataPrecision(Integer dataPrecision) {
		this.dataPrecision = dataPrecision;
	}

	public Integer getDataScale() {
		return dataScale;
	}

	public void setDataScale(Integer dataScale) {
		this.dataScale = dataScale;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public Boolean getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(Boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public ColumnConstraint getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(ColumnConstraint primaryKey) {
		this.primaryKey = primaryKey;
	}

	public ColumnConstraint getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(ColumnConstraint uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public ColumnConstraint getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(ColumnConstraint foreignKey) {
		this.foreignKey = foreignKey;
	}

	// -----------------------------------------

	public boolean isPrimary() {
		return primaryKey != null;
	}

	public boolean isMultiPrimary() {
		return primaryKey != null && primaryKey.getConstraintMode() == ConstraintMode.MULTIPLE;
	}

	public boolean isUnique() {
		return uniqueKey != null;
	}

	public boolean isMultiUnique() {
		return uniqueKey != null && uniqueKey.getConstraintMode() == ConstraintMode.MULTIPLE;
	}

	public boolean isForeignKey() {
		return foreignKey != null;
	}

	public boolean isMultiForeignKey() {
		return foreignKey != null && foreignKey.getConstraintMode() == ConstraintMode.MULTIPLE;
	}

	public boolean isUnsigned() {
		return unsigned;
	}

	public void setUnsigned(boolean unsigned) {
		this.unsigned = unsigned;
	}
	
	
}

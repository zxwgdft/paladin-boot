package com.paladin.data.controller.dto;

public class ColumnDTO {
	
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
	
	
	private boolean isPrimary;	
	private boolean isMultiPrimary;
	private boolean isUnique;
	private boolean isMultiUnique;
	private boolean isForeignKey;
	private boolean isMultiForeignKey;
	
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

	public boolean isUnsigned() {
		return unsigned;
	}

	public void setUnsigned(boolean unsigned) {
		this.unsigned = unsigned;
	}

	public boolean getIsPrimary() {
		return isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public boolean getIsMultiPrimary() {
		return isMultiPrimary;
	}

	public void setMultiPrimary(boolean isMultiPrimary) {
		this.isMultiPrimary = isMultiPrimary;
	}

	public boolean getIsUnique() {
		return isUnique;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

	public boolean getIsMultiUnique() {
		return isMultiUnique;
	}

	public void setMultiUnique(boolean isMultiUnique) {
		this.isMultiUnique = isMultiUnique;
	}

	public boolean getIsForeignKey() {
		return isForeignKey;
	}

	public void setForeignKey(boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
	}

	public boolean getIsMultiForeignKey() {
		return isMultiForeignKey;
	}

	public void setMultiForeignKey(boolean isMultiForeignKey) {
		this.isMultiForeignKey = isMultiForeignKey;
	}

	
}

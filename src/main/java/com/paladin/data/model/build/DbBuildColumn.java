package com.paladin.data.model.build;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class DbBuildColumn {

	public final static String COLUMN_FIELD_CONNECTION_NAME = "connectionName";
	public final static String COLUMN_FIELD_TABLE_NAME = "tableName";

	// 
	@Id
	@GeneratedValue(generator = "UUID")
	private String id;

	// 
	private String connectionName;

	// 
	private String tableName;

	// 
	private String columnName;

	// 标题
	private String title;

	// 常量CODE
	private String enumCode;

	// 是否多选
	private Integer multiSelect;

	// 是否附件
	private Integer isAttachment;

	// 附件数量
	private Integer attachmentCount;

	// 是否必填
	private Integer required;

	// 是否列表显示
	private Integer tableable;

	// 是否可编辑
	private Integer editable;

	// 是否新增
	private Integer addable;

	// 是否大文本
	private Integer largeText;

	// 最大长度
	private Integer maxLength;

	// 正则验证表达式
	private String regularExpression;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEnumCode() {
		return enumCode;
	}

	public void setEnumCode(String enumCode) {
		this.enumCode = enumCode;
	}

	public Integer getMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(Integer multiSelect) {
		this.multiSelect = multiSelect;
	}

	public Integer getIsAttachment() {
		return isAttachment;
	}

	public void setIsAttachment(Integer isAttachment) {
		this.isAttachment = isAttachment;
	}

	public Integer getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	public Integer getRequired() {
		return required;
	}

	public void setRequired(Integer required) {
		this.required = required;
	}

	public Integer getTableable() {
		return tableable;
	}

	public void setTableable(Integer tableable) {
		this.tableable = tableable;
	}

	public Integer getEditable() {
		return editable;
	}

	public void setEditable(Integer editable) {
		this.editable = editable;
	}

	public Integer getAddable() {
		return addable;
	}

	public void setAddable(Integer addable) {
		this.addable = addable;
	}

	public Integer getLargeText() {
		return largeText;
	}

	public void setLargeText(Integer largeText) {
		this.largeText = largeText;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

}
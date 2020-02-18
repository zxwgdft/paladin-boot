package com.paladin.data.service.vo;

import com.paladin.data.controller.dto.ColumnDTO;

public class BuildColumnVO {

	private ColumnDTO column;

	//
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

	public ColumnDTO getColumn() {
		return column;
	}

	public void setColumn(ColumnDTO column) {
		this.column = column;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

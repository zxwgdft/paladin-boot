package com.paladin.data.database.model.constraint;

public enum ConstraintType {
	/**
	 * 主键
	 */
	PRIMARY,
	/**
	 * 唯一键
	 */
	UNIQUE,
	/**
	 * 外键关联
	 */
	REFERENCE,
	/**
	 * 索引
	 */
	INDEX;
	
}

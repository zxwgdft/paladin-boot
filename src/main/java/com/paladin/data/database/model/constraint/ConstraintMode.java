package com.paladin.data.database.model.constraint;

public enum ConstraintMode {
	/**
	 * 单个键约束
	 */
	SINGLE,
	/**
	 * 组合键约束
	 */
	MULTIPLE,
	/**
	 * 无约束
	 */
	NULL;
}

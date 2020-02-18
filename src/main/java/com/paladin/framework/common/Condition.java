package com.paladin.framework.common;

public class Condition {
	String name;
	Object value;
	QueryType type;

	public Condition(String name, QueryType type, Object value) {
		this.name = name;
		this.value = value;
		this.type = type;
	}
}

package com.paladin.framework.service;

public class Condition {
	protected String name;
	protected Object value;
	protected QueryType type;

	public Condition(String name, QueryType type, Object value) {
		this.name = name;
		this.value = value;
		this.type = type;
	}
}

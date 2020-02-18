package com.paladin.data.database.model.constraint;

public class ColumnConstraint {
	String table;
	String column;
	
	String referencedTable;
	String referencedColumn;
	
	TableConstraint tableConstraint;
	
	ColumnConstraint(String table,String column,TableConstraint tableConstraint){
		this.table = table;
		this.column = column;
		this.tableConstraint = tableConstraint;
	}
	
	ColumnConstraint(String table,String column,TableConstraint tableConstraint,String referencedTable,String referencedColumn){
		this.table = table;
		this.column = column;
		this.tableConstraint = tableConstraint;
		this.referencedColumn = referencedColumn;
		this.referencedTable= referencedTable;
	}

	public String getTable() {
		return table;
	}

	public String getColumn() {
		return column;
	}

	public String getReferencedTable() {
		return referencedTable;
	}

	public String getReferencedColumn() {
		return referencedColumn;
	}

	public TableConstraint getTableConstraint() {
		return tableConstraint;
	}
	
	public ConstraintType getConstraintType() {
		return tableConstraint.constraintType;
	}

	public ConstraintMode getConstraintMode() {
		return tableConstraint.constraintMode;
	}
	
}

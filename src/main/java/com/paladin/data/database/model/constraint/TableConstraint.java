package com.paladin.data.database.model.constraint;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TableConstraint {

	String name;
	String tableName;
	ConstraintType constraintType;
	ConstraintMode constraintMode;

	List<ColumnConstraint> constraint;

	public TableConstraint(String name, String tableName,ConstraintType constraintType) {
		this.name = name;
		this.tableName = tableName;
		this.constraintType = constraintType;
		this.constraint = new CopyOnWriteArrayList<>();
		this.constraintMode = ConstraintMode.SINGLE;
	}

	public ColumnConstraint addConstraint(String table, String column) {
		ColumnConstraint columnConstraint = new ColumnConstraint(table, column, this);
		insertConstraint(columnConstraint);
		return columnConstraint;
	}

	public ColumnConstraint addRefrencedConstraint(String table, String column, String referencedTable, String referencedColumn) {
		ColumnConstraint columnConstraint = new ColumnConstraint(table, column, this, referencedTable, referencedColumn);
		insertConstraint(columnConstraint);
		return columnConstraint;
	}
	
	private void insertConstraint(ColumnConstraint columnConstraint){	
		constraint.add(columnConstraint);
		if (constraint.size() > 1)
			this.constraintMode = ConstraintMode.MULTIPLE;
	}

	public String getName() {
		return name;
	}

	public ConstraintType getConstraintType() {
		return constraintType;
	}

	public ConstraintMode getConstraintMode() {
		return constraintMode;
	}

	public ColumnConstraint[] getConstraint() {
		return constraint.toArray(new ColumnConstraint[constraint.size()]);
	}

}

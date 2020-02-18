package com.paladin.data.database.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import com.paladin.data.database.model.constraint.ConstraintType;
import com.paladin.data.database.model.constraint.TableConstraint;

public class Table extends PathContainer<Column> {

	String name;
	CopyOnWriteArrayList<TableConstraint> constraints = new CopyOnWriteArrayList<>();

	public Table(String name) {
		this.name = name;
	}

	public void addTableConstraint(TableConstraint tableConstraint) {
		this.constraints.add(tableConstraint);
	}

	public String getName() {
		return name;
	}

	public TableConstraint[] getConstraints() {
		return constraints.toArray(new TableConstraint[constraints.size()]);
	}
	
	public TableConstraint getPrimaryConstraint(){
		
		for(TableConstraint constraint : constraints)
		{
			if(constraint.getConstraintType() == ConstraintType.PRIMARY)
				return constraint;
		}
		
		return null;
	}

	protected Column[] sort(Column[] array) {

		Arrays.sort(array, new Comparator<Column>() {

			@Override
			public int compare(Column o1, Column o2) {
				int i1 = o1.getOrderIndex();
				int i2 = o2.getOrderIndex();

				if (i1 > i2)
					return 1;
				else if (i2 > i1)
					return -1;
				else
					return 0;
			}

		});

		return array;
	}

}

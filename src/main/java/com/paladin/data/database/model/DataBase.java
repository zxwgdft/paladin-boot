package com.paladin.data.database.model;

import java.util.Arrays;
import java.util.Comparator;

public class DataBase extends PathContainer<Table> {

    String name;

    public DataBase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected Table[] sort(Table[] array) {

        Arrays.sort(array, new Comparator<Table>() {

            @Override
            public int compare(Table o1, Table o2) {
                return o1.name.compareTo(o2.name);
            }

        });

        return array;
    }
}

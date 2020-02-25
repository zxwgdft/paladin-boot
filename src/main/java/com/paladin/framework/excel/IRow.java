package com.paladin.framework.excel;

public interface IRow {
    ICell getCell(int cellIndex);

    int getLastCellNum();
}

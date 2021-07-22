package com.paladin.common.core.export;

import com.paladin.framework.excel.write.WriteColumn;
import com.paladin.framework.excel.write.WriteRow;

import java.util.List;

public class SimpleWriteRow extends WriteRow {

    public SimpleWriteRow(List<WriteColumn> columns) {
        this.setColumns(columns);
    }

    @Override
    public Object peelData(Object data) {
        return data;
    }
}

package com.paladin.common.core.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paladin.framework.excel.write.CellStyleCreator;
import com.paladin.framework.excel.write.ValueFormatter;
import com.paladin.framework.utils.reflect.LambdaUtil;
import com.paladin.framework.utils.support.GetFunction;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ExportCondition {

    public final static String FILE_TYPE_EXCEL = "excel";

    public final static String SCOPE_ALL = "all";
    public final static String SCOPE_PAGE = "page";

    private String fileType;
    private String dataScope;
    private String sheetName;
    private String fileName;
    private List<ExportColumn> columns;

    @JsonIgnore
    private Map<String, ValueFormatter> valueFormatMap;
    @JsonIgnore
    private Map<String, CellStyleCreator> cellStyleCreatorMap;

    public boolean isExportPage() {
        return SCOPE_PAGE.equals(dataScope);
    }

    public boolean isExportAll() {
        return SCOPE_ALL.equals(dataScope);
    }

    public void putValueFormatter(String field, ValueFormatter formatter) {
        if (valueFormatMap == null) {
            valueFormatMap = new HashMap<>();
        }
        valueFormatMap.put(field, formatter);
    }

    public <T> void putValueFormatter(GetFunction<T> getFunction, ValueFormatter formatter) {
        if (valueFormatMap == null) {
            valueFormatMap = new HashMap<>();
        }
        valueFormatMap.put(LambdaUtil.getFieldNameByFunction(getFunction), formatter);
    }

    public void putCellStyleCreator(String field, CellStyleCreator creator) {
        if (cellStyleCreatorMap == null) {
            cellStyleCreatorMap = new HashMap<>();
        }
        cellStyleCreatorMap.put(field, creator);
    }

    public <T> void putCellStyleCreator(GetFunction<T> getFunction, CellStyleCreator creator) {
        if (cellStyleCreatorMap == null) {
            cellStyleCreatorMap = new HashMap<>();
        }
        cellStyleCreatorMap.put(LambdaUtil.getFieldNameByFunction(getFunction), creator);
    }

    @Getter
    @Setter
    public static class ExportColumn {
        private String field;
        private String name;
        private String dateFormat;
        private String enumType;
        private Boolean multiple;
        private Integer width;
        private String alignment;
    }

}

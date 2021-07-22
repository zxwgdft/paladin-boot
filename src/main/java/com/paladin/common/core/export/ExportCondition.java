package com.paladin.common.core.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paladin.framework.excel.write.CellStyleCreator;
import com.paladin.framework.excel.write.ValueFormatter;
import lombok.Getter;
import lombok.Setter;

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
    private Map<String, ValueFormatter> excelValueFormatMap;

    @JsonIgnore
    private Map<String, CellStyleCreator> cellStyleCreatorMap;


    public boolean isExportPage() {
        return SCOPE_PAGE.equals(dataScope);
    }

    public boolean isExportAll() {
        return SCOPE_ALL.equals(dataScope);
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
    }

}

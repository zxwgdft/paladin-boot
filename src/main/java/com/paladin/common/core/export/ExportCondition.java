package com.paladin.common.core.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paladin.framework.excel.write.ValueFormator;
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
    private List<ExportColumn> columns;

    @JsonIgnore
    private Map<String, ValueFormator> excelValueFormatMap;


    /**
     * 按照list顺序设置cell序号
     */
    public void sortCellIndex() {
        if (columns != null) {
            for (int i = 0; i < columns.size(); i++) {
                columns.get(i).setIndex(i);
            }
        }
    }

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
        private Integer index;
        private String dateFormat;
        private String enumType;
        private Boolean multiple;
        private Integer width;
    }

}

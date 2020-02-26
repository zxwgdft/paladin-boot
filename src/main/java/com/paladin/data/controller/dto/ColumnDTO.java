package com.paladin.data.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnDTO {

    /**
     * 列名
     */
    private String name;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据长度
     */
    private Integer dataLength;

    /**
     * 数据精度
     */
    private Integer dataPrecision;

    /**
     * 数据规模
     */
    private Integer dataScale;

    /**
     * 是否能为NULL
     */
    private boolean nullable;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 注释
     */
    private String comment;

    /**
     * 排序号
     */
    private Integer orderIndex;

    /**
     * 有无符号（用于MYSQL）
     */
    private boolean unsigned;
    private boolean isPrimary;
    private boolean isMultiPrimary;
    private boolean isUnique;
    private boolean isMultiUnique;
    private boolean isForeignKey;
    private boolean isMultiForeignKey;


}

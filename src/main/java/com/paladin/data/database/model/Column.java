package com.paladin.data.database.model;

import com.paladin.data.database.model.constraint.ColumnConstraint;
import com.paladin.data.database.model.constraint.ConstraintMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Column {

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

    // --------------- 约束 ------------------

    /*
     *
     * 如果有交集的组合唯一键或外键，则会出现一列由多个ColumnConstraint，
     * 这种情况会出现被替换的问题，但是这里不做这种特殊情况处理
     *
     */

    /**
     * 是否主键
     */
    private ColumnConstraint primaryKey;

    /**
     * 是否唯一
     */
    private ColumnConstraint uniqueKey;

    /**
     * 外键 ,只查找关联本数据库的外键
     */
    private ColumnConstraint foreignKey;

    /**
     * 是否自增 NULL为不确定
     */
    private Boolean autoIncrement;


    // -----------------------------------------

    public boolean isPrimary() {
        return primaryKey != null;
    }

    public boolean isMultiPrimary() {
        return primaryKey != null && primaryKey.getConstraintMode() == ConstraintMode.MULTIPLE;
    }

    public boolean isUnique() {
        return uniqueKey != null;
    }

    public boolean isMultiUnique() {
        return uniqueKey != null && uniqueKey.getConstraintMode() == ConstraintMode.MULTIPLE;
    }

    public boolean isForeignKey() {
        return foreignKey != null;
    }

    public boolean isMultiForeignKey() {
        return foreignKey != null && foreignKey.getConstraintMode() == ConstraintMode.MULTIPLE;
    }

}

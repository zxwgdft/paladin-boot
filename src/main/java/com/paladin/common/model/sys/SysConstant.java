package com.paladin.common.model.sys;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
public class SysConstant {

    public final static String COLUMN_FIELD_ORDER_NO = "orderNo";
    public final static String COLUMN_FIELD_TYPE = "type";

    @Id
    private String type;

    @Id
    private String code;

    private String name;

    private Integer orderNo;

}
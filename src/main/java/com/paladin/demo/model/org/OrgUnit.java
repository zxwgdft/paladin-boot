package com.paladin.demo.model.org;

import com.paladin.framework.api.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
public class OrgUnit extends BaseModel {

    public static final String FIELD_ORDER_NO = "orderNo";
    // 主键
    @Id
    private String id;

    // 单位名称
    private String name;

    // 单位类型
    private Integer type;

    // 上级单位
    private String parentId;

    // 联系人
    private String contact;

    // 联系电话
    private String contactPhone;

    // 排序号
    private Integer orderNo;

}
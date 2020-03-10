package com.paladin.demo.service.org.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrgUnitDTO {

    // 主键
    private String id;

    // 单位名称
    @NotEmpty(message = "单位名称不能为空")
    @Length(max = 120, message = "单位名称长度不能大于120")
    private String name;

    // 单位类型
    @NotNull(message = "单位类型不能为空")
    private Integer type;

    // 上级单位
    @Length(max = 32, message = "上级单位长度不能大于32")
    private String parentId;

    // 联系人
    @Length(max = 50, message = "联系人长度不能大于50")
    private String contact;

    // 联系电话
    @Length(max = 50, message = "联系电话长度不能大于50")
    private String contactPhone;

    // 排序号
    @NotNull(message = "排序号不能为空")
    private Integer orderNo;

}
package com.paladin.demo.service.org.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrgPersonnelVO {

    //
    private String id;

    // 所属机构
    private Integer agencyId;

    // 机构名称
    private String agencyName;

    // 证件类型
    private Integer identificationType;

    // 证件号码
    private String identificationNo;

    // 姓名
    private String name;

    // 性别
    private Integer sex;

    // 出生日期
    private Date birthday;

    // 手机号码
    private String cellphone;

    // 电话号码
    private String officePhone;

    // 民族
    private Integer nation;


}
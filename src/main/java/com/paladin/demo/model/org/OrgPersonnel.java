package com.paladin.demo.model.org;

import com.paladin.framework.api.BaseModel;
import com.paladin.framework.service.IgnoreSelection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
public class OrgPersonnel extends BaseModel {

    public static final String FIELD_IDENTIFICATION_NO = "identificationNo";

    public final static String FIELD_UNIT_ID = "unitId";
    public static final String FIELD_ID = "id";

    //
    @Id
    private String id;

    // 所属机构
    private String unitId;

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

    // 账号
    private String account;

    // 角色
    private String roles;

    // 简历
    @IgnoreSelection
    private String resume;

    // 附件
    @IgnoreSelection
    private String attachment;

}
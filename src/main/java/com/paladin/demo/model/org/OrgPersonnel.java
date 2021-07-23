package com.paladin.demo.model.org;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.paladin.framework.api.DeletedBaseModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrgPersonnel extends DeletedBaseModel {

    public static final String FIELD_IDENTIFICATION_NO = "identificationNo";
    //
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    // 所属机构
    private Integer agencyId;

    // 证件类型
    private Integer identificationType;

    // 证件号码
    private String identificationNo;

    // 姓名
    private String name;

    // 姓名拼音首字母
    private String pinyinName;

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
    @TableField(select = false)
    private String resume;

    // 附件
    @TableField(select = false)
    private String attachment;

}
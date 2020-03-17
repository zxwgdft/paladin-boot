package com.paladin.demo.service.org.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class OrgPersonnelDTO {

    //
    private String id;

    // 账号
    @NotEmpty(message = "账号不能为空")
    @Length(min = 6, max = 20, message = "账号长度必须在6-20位")
    private String account;

    // 所属机构
    @NotEmpty(message = "所属机构不能为空")
    @Length(max = 32, message = "所属机构长度不能大于32")
    private String unitId;

    // 证件类型
    @NotNull(message = "证件类型不能为空")
    private Integer identificationType;

    // 证件号码
    @NotEmpty(message = "证件号码不能为空")
    @Length(max = 32, message = "证件号码长度不能大于32")
    private String identificationNo;

    // 姓名
    @NotEmpty(message = "姓名不能为空")
    @Length(max = 20, message = "姓名长度不能大于20")
    private String name;

    // 性别
    private Integer sex;

    // 出生日期
    private Date birthday;

    // 手机号码
    @Length(max = 20, message = "手机号码长度不能大于20")
    private String cellphone;

    // 电话号码
    @Length(max = 20, message = "电话号码长度不能大于20")
    private String officePhone;

    // 民族
    private Integer nation;

    // 角色
    @Length(max = 200, message = "角色选择过多")
    private String roles;

    // 简历
    @Length(max = 10000, message = "简历长度不能大于10000")
    private String resume;

    // 附件
    @Length(max = 200, message = "附件个数不能超过5个")
    private String attachment;

    // 附件文件数据
    private MultipartFile[] attachmentFiles;

}
package com.paladin.demo.service.org.vo;

import com.paladin.common.core.FileResourceContainer;
import com.paladin.common.service.sys.vo.FileResource;
import com.paladin.demo.service.org.OrgUnitContainer;
import com.paladin.framework.service.IgnoreSelection;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrgPersonnelVO {


    //
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
    @IgnoreSelection
    private String roles;

    // 简历
    @IgnoreSelection
    private String resume;

    // 附件
    @IgnoreSelection
    private String attachment;

    // 可以不写该变量，返回json时仍旧会根据getUnitName方法返回数据，这里写是为了导出excel用
    private String unitName;

    // 返回单位名称
    public String getUnitName() {
        return OrgUnitContainer.getUnitName(unitId);
    }

    // 返回附件文件信息
    public List<FileResource> getAttachmentFiles() {
        if (attachment != null && attachment.length() > 0) {
            return FileResourceContainer.getFileResources(attachment.split(","));
        }
        return null;
    }

}
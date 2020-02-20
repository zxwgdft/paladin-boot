package com.paladin.demo.model.org;

import com.paladin.framework.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
public class DemoPersonnel extends BaseModel {

    public final static String FIELD_IDENTIFICATIONNO = "identificationNo";
    public final static String FIELD_SEX = "sex";

    //
    @Id
    private String id;

    // 所属机构
    private String agencyId;

    // 机构名称
    private String agencyName;

    // 证件类型
    private Integer identificationType;

    // 证件号码
    private String identificationNo;

    //
    private String name;

    //
    private Integer sex;

    //
    private Date birthday;

    //
    private String cellphone;

    //
    private String officePhone;

    // 头像
    private String profilePhoto;

    // 民族
    private Integer nation;

    // 开始工作时间
    private Date startWorkTime;

    // 加入党派时间
    private Date joinPartyTime;

    // 政治面貌
    private Integer politicalAffiliation;

    // 籍贯
    private String nativePlace;


}
package com.paladin.demo.service.org.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DemoPersonnelDTO {

    //
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

    private MultipartFile profilePhotoFile;


}
package com.paladin.demo.model.org;

import com.paladin.framework.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
public class DemoAgency extends BaseModel {

    // 主键
    @Id
    private String id;

    // 执业许可证
    private String licenseNo;

    // 执业许可证照片
    private String license;

    // 机构名称
    private String name;

    // 机构类型
    private String agencyType;

    // 地址
    private String address;

    // 成立日期
    private Date registerTime;

    // 统一社会信用代码
    private String socialCreditCode;

    // 经营范围
    private String businessScope;

    // 联系方式
    private String contactWay;

    // 法定代表人或负责人
    private String chargePerson;

    //
    private String chargePersonId;

    // 人事信息
    private String personnelInformation;


}
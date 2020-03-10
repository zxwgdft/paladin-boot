package com.paladin.demo.model.org;

import com.paladin.framework.common.BaseModel;
import java.util.Date;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class OrgPersonnel extends BaseModel {

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

}
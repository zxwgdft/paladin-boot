package com.paladin.demo.service.org.dto;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter 
@Setter 
public class OrgPersonnelDTO {

	// 
	private String id;

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

}
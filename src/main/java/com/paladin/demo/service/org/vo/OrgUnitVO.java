package com.paladin.demo.service.org.vo;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class OrgUnitVO {

	// 主键
	private String id;

	// 单位名称
	private String name;

	// 单位类型
	private Integer type;

	// 上级单位
	private String parentId;

	// 联系人
	private String contact;

	// 联系电话
	private String contactPhone;

	// 排序号
	private Integer orderNo;

}
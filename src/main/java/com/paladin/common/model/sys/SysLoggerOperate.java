package com.paladin.common.model.sys;

import java.util.Date;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class SysLoggerOperate {

	// 
	@Id
	private String id;

	// 模块名称
	private String modelName;

	// 操作名称
	private String operateName;

	// 类名称
	private String className;

	// 方法名称
	private String methodName;

	// 是否成功
	private Boolean isSuccess;

	// 错误信息
	private String errorMessage;

	// 操作人
	private String operateBy;

	// 操作人名称
	private String operateByName;

	// 操作时间
	private Date operateTime;

	// 操作时长
	private Integer operateDuration;

}
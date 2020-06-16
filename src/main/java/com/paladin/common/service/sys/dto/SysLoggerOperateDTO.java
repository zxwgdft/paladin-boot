package com.paladin.common.service.sys.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter 
@Setter 
public class SysLoggerOperateDTO {

	// 
	private String id;

	// 模块名称
	@Length(max = 30, message = "模块名称长度不能大于30")
	private String modelName;

	// 操作名称
	@Length(max = 50, message = "操作名称长度不能大于50")
	private String operateName;

	// 类名称
	@Length(max = 100, message = "类名称长度不能大于100")
	private String className;

	// 方法名称
	@Length(max = 40, message = "方法名称长度不能大于40")
	private String methodName;

	// 是否成功
	private Boolean isSuccess;

	// 错误信息
	@Length(max = 255, message = "错误信息长度不能大于255")
	private String errorMessage;

	// 操作人
	@Length(max = 32, message = "操作人长度不能大于32")
	private String operateBy;

	// 操作人名称
	@Length(max = 50, message = "操作人名称长度不能大于50")
	private String operateByName;

	// 操作时间
	private Date operateTime;

	// 操作时长
	private Integer operateDuration;

}
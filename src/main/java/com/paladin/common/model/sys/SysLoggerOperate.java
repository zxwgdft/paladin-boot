package com.paladin.common.model.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel(description = "操作日志")
public class SysLoggerOperate {

    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("模块名称")
    private String modelName;

    @ApiModelProperty("操作名称")
    private String operateName;

    @ApiModelProperty("类名称")
    private String className;

    @ApiModelProperty("方法名称")
    private String methodName;

    @ApiModelProperty("是否成功")
    private Boolean isSuccess;

    @ApiModelProperty("错误信息")
    private String errorMessage;

    @ApiModelProperty("操作人")
    private String operateBy;

    @ApiModelProperty("操作人名称")
    private String operateByName;

    @ApiModelProperty("操作时间")
    private Date operateTime;

    @ApiModelProperty("操作时长")
    private Integer operateDuration;

}
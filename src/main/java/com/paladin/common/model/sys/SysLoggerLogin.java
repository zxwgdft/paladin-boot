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
@ApiModel(description = "登录日志")
public class SysLoggerLogin {


    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("IP")
    private String ip;

    @ApiModelProperty("登录方式")
    private Integer loginType;

    @ApiModelProperty("登录账号")
    private String account;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("登录时间")
    private Date createTime;


}
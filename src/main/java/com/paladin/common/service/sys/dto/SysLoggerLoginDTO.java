package com.paladin.common.service.sys.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysLoggerLoginDTO {

    //
    private String id;

    //
    private String ip;

    // 登录方式
    private Integer loginType;

    // 登录账号
    private String account;

    // 用户ID
    private String userId;

    // 用户类型
    private Integer userType;

    //
    private Date createTime;


}
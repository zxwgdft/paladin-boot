package com.paladin.demo.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author TontoZhou
 * @since 2019/12/26
 */
@Getter
@Setter
@ApiModel(description = "登录结果")
public class LoginResult implements Serializable {

    @ApiModelProperty("是否成功")
    private boolean success;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("用户类型")
    private int userType;
    @ApiModelProperty("认证token")
    private String token;
    @ApiModelProperty("是否系统管理员")
    private boolean isSystemAdmin;

    public LoginResult(boolean success) {
        this.success = success;
    }

    public LoginResult() {

    }
}

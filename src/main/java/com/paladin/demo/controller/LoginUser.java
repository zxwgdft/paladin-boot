package com.paladin.demo.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2019/12/26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "登录用户")
public class LoginUser {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("验证码")
    private String code;//验证码

    @ApiModelProperty("是否记住")
    private boolean rememberMe;


}

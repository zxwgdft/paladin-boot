package com.paladin.demo.controller;

import com.paladin.common.service.syst.SysUserService;
import com.paladin.framework.core.exception.BusinessException;
import com.paladin.platform.PlatformUserToken;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author TontoZhou
 * @since 2020/1/19
 */
@Controller
@RequestMapping("/demo")
public class PlatformController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "登录页面")
    @GetMapping("/platform/login")
    public String loginFromPlatform(String userId, String token) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new PlatformUserToken(userId, token));
        if (subject.isAuthenticated()) {
            return "redirect:/demo/index";
        } else {
            throw new BusinessException("平台登录失败，请确认是否有权限");
        }
    }

}

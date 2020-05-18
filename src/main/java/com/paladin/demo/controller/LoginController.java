package com.paladin.demo.controller;

import com.paladin.common.core.permission.Menu;
import com.paladin.common.service.sys.SysUserService;
import com.paladin.demo.core.DemoUserSession;
import com.paladin.framework.GlobalProperties;
import com.paladin.framework.common.R;
import com.paladin.framework.service.UserSession;
import com.paladin.framework.shiro.filter.PaladinFormAuthenticationFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Api("用户认证模块")
@Controller
@RequestMapping("/demo")
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "主页面")
    @GetMapping(value = "/index")
    public Object main(HttpServletRequest request) {
        DemoUserSession userSession = DemoUserSession.getCurrentUserSession();
        ModelAndView model = new ModelAndView("/" + GlobalProperties.project + "/index");
        model.addObject("name", userSession.getUserName());

        Collection<Menu> menus = userSession.getMenuResources();
        StringBuilder sb = new StringBuilder("<li class=\"header\">菜单</li>");
        createMenuHtml(menus, sb);
        model.addObject("menuHtml", sb.toString());
        return model;
    }

    private void createMenuHtml(Collection<Menu> menus, StringBuilder sb) {
        for (Menu menu : menus) {
            Collection<Menu> children = menu.getChildren();
            String href = menu.isOwned() ? menu.getUrl() : null;

            String icon = menu.getIcon();
            if (icon != null && icon.length() > 0) {
                icon = "fa iconfont icon" + icon;
            } else {
                icon = "fa fa-circle-o";
            }

            if (children.size() > 0) {
                sb.append("<li class=\"treeview\"><a class=\"nav-link\"");
                if (href != null && href.length() > 0) {
                    sb.append(" onclick=\"goto({id:'").append(menu.getId()).append("',title: '").append(menu.getName()).append("',url: '")
                            .append(href).append("'});\"");
                }

                sb.append("><i class=\"").append(icon).append("\"></i><span>").append(menu.getName()).append(
                        "</span><span class=\"pull-right-container\"><i class=\"fa fa-angle-left pull-right\"></i></span></a><ul class=\"treeview-menu\">");
                createMenuHtml(children, sb);
                sb.append("</ul></li>");
            } else {
                sb.append("<li><a class=\"nav-link\"");
                if (href != null) {
                    sb.append(" onclick=\"goto({id:'").append(menu.getId()).append("',title: '").append(menu.getName()).append("',url: '")
                            .append(href).append("'});\"");
                }
                sb.append("><i class=\"").append(icon).append("\"></i> <span>").append(menu.getName()).append("</span></a></li>");
            }
        }
    }

    @ApiOperation("修改密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "newPassword", value = "新密码", required = true), @ApiImplicitParam(name = "oldPassword", value = "旧密码")})
    @RequestMapping(value = "/update/password", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object updatePassword(@RequestParam String newPassword, @RequestParam String oldPassword) {
        return sysUserService.updateSelfPassword(newPassword, oldPassword) ? R.success() : R.fail("修改失败");
    }

    @ApiOperation(value = "登录页面")
    @GetMapping("/login")
    public Object loginInput(HttpServletRequest request, Model model) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return main(request);
        }
        return "/" + GlobalProperties.project + "/login";
    }

    @ApiOperation(value = "用户认证")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public Object login(HttpServletRequest request, HttpServletResponse response, Model model) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return main(request);
        } else {
            model.addAttribute("isError", true);
            String errorMsg = (String) request.getAttribute(PaladinFormAuthenticationFilter.ERROR_KEY_LOGIN_FAIL_MESSAGE);
            model.addAttribute("errorMsg", errorMsg == null ? "登录失败" : errorMsg);
            return "/" + GlobalProperties.project + "/login";
        }
    }

    @ApiOperation(value = "用户认证")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object ajaxLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return R.success(UserSession.getCurrentUserSession().getUserForView());
        } else {
            String errorMsg = (String) request.getAttribute(PaladinFormAuthenticationFilter.ERROR_KEY_LOGIN_FAIL_MESSAGE);
            return R.fail(errorMsg == null ? "登录失败" : errorMsg);
        }
    }

    @GetMapping(value = "/time")
    public String time() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}

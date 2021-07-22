package com.paladin.common.controller.org;

import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.security.NeedPermission;
import com.paladin.common.model.org.OrgRole;
import com.paladin.common.service.org.OrgPermissionService;
import com.paladin.common.service.org.OrgRolePermissionService;
import com.paladin.common.service.org.OrgRoleService;
import com.paladin.common.service.org.dto.OrgRoleDTO;
import com.paladin.common.service.org.dto.OrgRoleQueryDTO;
import com.paladin.framework.api.R;
import com.paladin.framework.service.PageResult;
import com.paladin.framework.utils.UUIDUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@Controller
@RequestMapping("/common/org/role")
public class OrgRoleController extends ControllerSupport {

    @Autowired
    private OrgRoleService orgRoleService;

    @Autowired
    private OrgPermissionService orgPermissionService;

    @Autowired
    private OrgRolePermissionService orgRolePermissionService;

    @GetMapping("/index")
    public String index() {
        return "/common/org/role_index";
    }

    @PostMapping("/find/page")
    @ResponseBody
    public PageResult<OrgRole> findPage(OrgRoleQueryDTO query) {
        return orgRoleService.findPage(query);
    }

    @PostMapping("/find/all")
    @ResponseBody
    public List<OrgRole> findAll(OrgRoleQueryDTO query) {
        return orgRoleService.findList(query);
    }

    @GetMapping("/get")
    @ResponseBody
    public OrgRole getDetail(@RequestParam String id) {
        return orgRoleService.get(id);
    }

    @GetMapping("/add")
    public String addInput() {
        return "/common/org/role_add";
    }

    @RequestMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
        model.addAttribute("id", id);
        return "/common/org/role_detail";
    }

    @PostMapping("/save")
    @ResponseBody
    @NeedPermission("sys:role:save")
    public R save(@Valid OrgRoleDTO orgRoleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        orgRoleService.saveRole(orgRoleDTO);
        return R.success();
    }

    @PostMapping("/update")
    @ResponseBody
    @NeedPermission("sys:role:update")
    public R update(@Valid OrgRoleDTO orgRoleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        orgRoleService.updateRole(orgRoleDTO);
        return R.success();
    }

    @GetMapping("/grant/index")
    public String grantAuthorizationInput(@RequestParam String id, Model model) {
        model.addAttribute("roleId", id);
        return "/common/org/role_grant";
    }

    @PostMapping("/grant/find/permission")
    @ResponseBody
    public Object getGrantAuthorization(@RequestParam String id, Model model) {
        Map<String, Object> result = new HashMap<>();
        result.put("permissions", orgPermissionService.findGrantablePermission());
        result.put("hasPermissions", orgRolePermissionService.getPermissionByRole(id));
        return result;
    }

    @PostMapping("/grant")
    @ResponseBody
    @NeedPermission("sys:role:grant")
    public R grantAuthorization(@RequestParam("roleId") String roleId, @RequestParam(required = false, name = "permissionId[]") String[] permissionIds) {
        orgRolePermissionService.grantAuthorization(roleId, permissionIds);
        return R.success();
    }
}
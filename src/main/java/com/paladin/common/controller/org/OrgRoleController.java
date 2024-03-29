package com.paladin.common.controller.org;

import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.security.NeedPermission;
import com.paladin.common.model.org.OrgRole;
import com.paladin.common.service.org.*;
import com.paladin.common.service.org.dto.OrgRoleDTO;
import com.paladin.common.service.org.dto.OrgRoleQueryDTO;
import com.paladin.framework.api.R;
import com.paladin.framework.service.PageResult;
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
    private OrgMenuService orgMenuService;

    @Autowired
    private OrgRolePermissionService orgRolePermissionService;

    @Autowired
    private OrgRoleMenuService orgRoleMenuService;

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
    public OrgRole getDetail(@RequestParam int id) {
        return orgRoleService.get(id);
    }

    @GetMapping("/add")
    public String addInput() {
        return "/common/org/role_add";
    }

    @GetMapping("/edit")
    public String detailInput(@RequestParam int id, Model model) {
        model.addAttribute("id", id);
        return "/common/org/role_edit";
    }

    @PostMapping("/save")
    @ResponseBody
    @NeedPermission("sys:role:save")
    public R save(@Valid OrgRoleDTO orgRoleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        orgRoleService.saveRole(orgRoleDTO);
        return R.SUCCESS;
    }

    @PostMapping("/update")
    @ResponseBody
    @NeedPermission("sys:role:update")
    public R update(@Valid OrgRoleDTO orgRoleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        orgRoleService.updateRole(orgRoleDTO);
        return R.SUCCESS;
    }

    @PostMapping("/disabled")
    @ResponseBody
    @NeedPermission("sys:role:update")
    public R disabled(@RequestParam int id) {
        orgRoleService.updateRoleDisabled(id);
        return R.SUCCESS;
    }

    @PostMapping("/enabled")
    @ResponseBody
    @NeedPermission("sys:role:update")
    public R enabled(@RequestParam int id) {
        orgRoleService.updateRoleEnabled(id);
        return R.SUCCESS;
    }

    @GetMapping("/grant/index")
    public String grantPermissionInput(@RequestParam String id, Model model) {
        model.addAttribute("roleId", id);
        return "/common/org/role_grant";
    }

    @PostMapping("/grant/data")
    @ResponseBody
    public Object getGrantPermission(@RequestParam int id) {
        Map<String, Object> result = new HashMap<>();
        result.put("permissions", orgPermissionService.findPermission4Grant());
        result.put("hasPermissions", orgRolePermissionService.getPermissionByRole(id));

        result.put("menus", orgMenuService.findMenu4Grant());
        result.put("hasMenus", orgRoleMenuService.getMenuByRole(id));

        return result;
    }

    @PostMapping("/grant/do/permission")
    @ResponseBody
    @NeedPermission("sys:role:grant")
    public R grantPermission(@RequestParam("roleId") int roleId, @RequestParam(required = false, name = "permissionId[]") int[] permissionIds) {
        orgRolePermissionService.grantAuthorization(roleId, permissionIds);
        return R.SUCCESS;
    }

    @PostMapping("/grant/do/menu")
    @ResponseBody
    @NeedPermission("sys:role:grant")
    public R grantMenu(@RequestParam("roleId") int roleId, @RequestParam(required = false, name = "menuId[]") int[] menuIds) {
        orgRoleMenuService.grantAuthorization(roleId, menuIds);
        return R.SUCCESS;
    }
}
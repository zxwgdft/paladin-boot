package com.paladin.common.controller.org;

import com.paladin.common.core.ControllerSupport;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @RequestMapping("/index")
    public String index() {
        return "/common/org/role_index";
    }

    @RequestMapping("/find/page")
    @ResponseBody
    public PageResult<OrgRole> findPage(OrgRoleQueryDTO query) {
        return orgRoleService.findPage(query);
    }

    @RequestMapping("/find/all")
    @ResponseBody
    public List<OrgRole> findAll(OrgRoleQueryDTO query) {
        return orgRoleService.findList(query);
    }

    @RequestMapping("/get")
    @ResponseBody
    public OrgRole getDetail(@RequestParam String id) {
        return orgRoleService.get(id);
    }

    @RequestMapping("/add")
    public String addInput() {
        return "/common/org/role_add";
    }

    @RequestMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
        model.addAttribute("id", id);
        return "/common/org/role_detail";
    }

    @RequestMapping("/save")
    @ResponseBody
    @RequiresPermissions("sys:role:save")
    public OrgRole save(@Valid OrgRoleDTO orgRoleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        String id = UUIDUtil.createUUID();
        orgRoleDTO.setId(id);
        orgRoleService.saveRole(orgRoleDTO);
        return orgRoleService.get(id);
    }

    @RequestMapping("/update")
    @ResponseBody
    @RequiresPermissions("sys:role:update")
    public OrgRole update(@Valid OrgRoleDTO orgRoleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        String id = orgRoleDTO.getId();
        orgRoleService.updateRole(orgRoleDTO);
        return orgRoleService.get(id);
    }

    @RequestMapping("/grant/index")
    public String grantAuthorizationInput(@RequestParam String id, Model model) {
        model.addAttribute("roleId", id);
        return "/common/org/role_grant";
    }

    @RequestMapping("/grant/find/permission")
    @ResponseBody
    public Object getGrantAuthorization(@RequestParam String id, Model model) {
        Map<String, Object> result = new HashMap<>();
        result.put("permissions", orgPermissionService.findGrantablePermission());
        result.put("hasPermissions", orgRolePermissionService.getPermissionByRole(id));
        return result;
    }

    @RequestMapping("/grant")
    @ResponseBody
    @RequiresPermissions("sys:role:grant")
    public R grantAuthorization(@RequestParam("roleId") String roleId, @RequestParam(required = false, name = "permissionId[]") String[] permissionIds) {
        orgRolePermissionService.grantAuthorization(roleId, permissionIds);
        return R.success();
    }
}
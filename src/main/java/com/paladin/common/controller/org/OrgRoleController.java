package com.paladin.common.controller.org;

import com.paladin.common.core.ControllerSupport;
import com.paladin.common.service.org.OrgPermissionService;
import com.paladin.common.service.org.OrgRolePermissionService;
import com.paladin.common.service.org.OrgRoleService;
import com.paladin.common.service.org.dto.OrgRoleDTO;
import com.paladin.common.service.org.dto.OrgRoleQueryDTO;
import com.paladin.common.service.org.vo.OrgRoleVO;
import com.paladin.framework.common.R;
import com.paladin.framework.utils.UUIDUtil;
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

    @RequestMapping("/find/all")
    @ResponseBody
    public Object findAll(OrgRoleQueryDTO query) {
        return R.success(orgRoleService.searchPage(query));
    }

    @RequestMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {
        return R.success(beanCopy(orgRoleService.get(id), new OrgRoleVO()));
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
    public Object save(@Valid OrgRoleDTO orgRoleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validErrorHandler(bindingResult);
        }
        String id = UUIDUtil.createUUID();
        orgRoleDTO.setId(id);
        if (orgRoleService.saveRole(orgRoleDTO)) {
            return R.success(beanCopy(orgRoleService.get(id), new OrgRoleVO()));
        }
        return R.fail("保存失败");
    }

    @RequestMapping("/update")
    @ResponseBody
    public Object update(@Valid OrgRoleDTO orgRoleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validErrorHandler(bindingResult);
        }
        String id = orgRoleDTO.getId();
        if (orgRoleService.updateRole(orgRoleDTO)) {
            return R.success(beanCopy(orgRoleService.get(id), new OrgRoleVO()));
        }
        return R.fail("更新失败");
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
        return R.success(result);
    }

    @RequestMapping("/grant")
    @ResponseBody
    public Object grantAuthorization(@RequestParam("roleId") String roleId, @RequestParam(required = false, name = "permissionId[]") String[] permissionIds) {
        return orgRolePermissionService.grantAuthorization(roleId, permissionIds) ? R.success() : R.fail("授权失败");
    }
}
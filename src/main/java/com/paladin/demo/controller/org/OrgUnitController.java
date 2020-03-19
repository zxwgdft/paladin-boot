package com.paladin.demo.controller.org;

import com.paladin.common.core.ControllerSupport;
import com.paladin.demo.model.org.OrgUnit;
import com.paladin.demo.service.org.OrgUnitContainer;
import com.paladin.demo.service.org.OrgUnitService;
import com.paladin.demo.service.org.dto.OrgUnitDTO;
import com.paladin.demo.service.org.vo.OrgUnitVO;
import com.paladin.framework.common.R;
import com.paladin.framework.utils.UUIDUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/demo/org/unit")
public class OrgUnitController extends ControllerSupport {

    @Autowired
    private OrgUnitService orgUnitService;

    @GetMapping("/index")
    public String index() {
        return "/demo/org/org_unit_index";
    }

    @RequestMapping(value = "/find", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object findPage() {
        return R.success(orgUnitService.findAll());
    }

    // 获取树形结构所有单位信息
    @GetMapping("/find/tree")
    @ResponseBody
    public Object findTree() {
        return R.success(OrgUnitContainer.getUnitTree());
    }

    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {
        return R.success(orgUnitService.get(id, OrgUnitVO.class));
    }

    @GetMapping("/add")
    public String addInput() {
        return "/demo/org/org_unit_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
        model.addAttribute("id", id);
        return "/demo/org/org_unit_detail";
    }

    @PostMapping("/save")
    @ResponseBody
    @RequiresPermissions("org:unit:save")
    public Object save(@Valid OrgUnitDTO orgUnitDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validErrorHandler(bindingResult);
        }
        OrgUnit model = beanCopy(orgUnitDTO, new OrgUnit());
        String id = UUIDUtil.createUUID();
        model.setId(id);
        if (orgUnitService.saveUnit(model)) {
            return R.success(orgUnitService.get(id, OrgUnitVO.class));
        }
        return R.fail("保存失败");
    }

    @PostMapping("/update")
    @ResponseBody
    @RequiresPermissions("org:unit:update")
    public Object update(@Valid OrgUnitDTO orgUnitDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validErrorHandler(bindingResult);
        }
        String id = orgUnitDTO.getId();
        OrgUnit model = beanCopy(orgUnitDTO, orgUnitService.get(id));
        if (orgUnitService.updateUnit(model)) {
            return R.success(orgUnitService.get(id, OrgUnitVO.class));
        }
        return R.fail("更新失败");
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @RequiresPermissions("org:unit:delete")
    public Object delete(@RequestParam String id) {
        return orgUnitService.removeUnit(id) ? R.success() : R.fail("保存失败");
    }


}
package com.paladin.demo.controller.org;

import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.common.core.log.OperationLog;
import com.paladin.demo.model.org.OrgUnit;
import com.paladin.demo.service.org.OrgUnitContainer;
import com.paladin.demo.service.org.OrgUnitService;
import com.paladin.demo.service.org.dto.OrgUnitDTO;
import com.paladin.framework.api.R;
import com.paladin.framework.utils.UUIDUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public List<OrgUnit> findPage() {
        return orgUnitService.findList();
    }

    // 获取树形结构所有单位信息
    @GetMapping("/find/tree")
    @ResponseBody
    public List<OrgUnitContainer.Unit> findTree() {
        return DataCacheHelper.getData(OrgUnitContainer.class).getUnitTree();
    }

    @GetMapping("/get")
    @ResponseBody
    public OrgUnit getDetail(@RequestParam String id, Model model) {
        return orgUnitService.get(id);
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
    @OperationLog(model = "机构管理", operate = "机构新增")
    public OrgUnit save(@Valid OrgUnitDTO orgUnitDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        OrgUnit model = beanCopy(orgUnitDTO, new OrgUnit());
        String id = UUIDUtil.createUUID();
        model.setId(id);
        orgUnitService.saveUnit(model);
        return orgUnitService.get(id);
    }

    @PostMapping("/update")
    @ResponseBody
    @RequiresPermissions("org:unit:update")
    @OperationLog(model = "机构管理", operate = "机构更新")
    public OrgUnit update(@Valid OrgUnitDTO orgUnitDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        String id = orgUnitDTO.getId();
        OrgUnit model = beanCopy(orgUnitDTO, orgUnitService.get(id));
        orgUnitService.updateUnit(model);
        return orgUnitService.get(id);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @RequiresPermissions("org:unit:delete")
    @OperationLog(model = "机构管理", operate = "机构删除")
    public R delete(@RequestParam String id) {
        orgUnitService.removeUnit(id);
        return R.success();
    }


}
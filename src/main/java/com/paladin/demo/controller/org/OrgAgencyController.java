package com.paladin.demo.controller.org;

import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.common.core.log.OperationLog;
import com.paladin.common.core.security.NeedPermission;
import com.paladin.demo.model.org.OrgAgency;
import com.paladin.demo.service.org.OrgAgencyContainer;
import com.paladin.demo.service.org.OrgAgencyService;
import com.paladin.demo.service.org.dto.OrgAgencyDTO;
import com.paladin.framework.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/demo/org/agency")
public class OrgAgencyController extends ControllerSupport {

    @Autowired
    private OrgAgencyService orgAgencyService;

    @GetMapping("/index")
    public String index() {
        return "/demo/org/org_agency_index";
    }

    @PostMapping(value = "/find")
    @ResponseBody
    public List<OrgAgency> findPage() {
        return orgAgencyService.findList();
    }

    // 获取树形结构所有单位信息
    @GetMapping("/find/tree")
    @ResponseBody
    public List<OrgAgencyContainer.Agency> findTree() {
        return DataCacheHelper.getData(OrgAgencyContainer.class).getAgencyTree();
    }

    @GetMapping("/get")
    @ResponseBody
    public OrgAgency getDetail(@RequestParam int id) {
        return orgAgencyService.get(id);
    }

    @GetMapping("/add")
    public String addInput() {
        return "/demo/org/org_agency_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam int id, Model model) {
        model.addAttribute("id", id);
        return "/demo/org/org_agency_detail";
    }

    @PostMapping("/save")
    @ResponseBody
    @NeedPermission("org:agency:save")
    @OperationLog(model = "机构管理", operate = "机构新增")
    public R save(@Valid OrgAgencyDTO orgagencyDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        orgAgencyService.saveAgency(orgagencyDTO);
        return R.success();
    }

    @PostMapping("/update")
    @ResponseBody
    @NeedPermission("org:agency:update")
    @OperationLog(model = "机构管理", operate = "机构更新")
    public R update(@Valid OrgAgencyDTO orgagencyDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        orgAgencyService.updateAgency(orgagencyDTO);
        return R.success();
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    @NeedPermission("org:agency:delete")
    @OperationLog(model = "机构管理", operate = "机构删除")
    public R delete(@RequestParam int id) {
        orgAgencyService.removeAgency(id);
        return R.success();
    }


}
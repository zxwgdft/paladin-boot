package com.paladin.demo.controller.org;

import com.paladin.demo.controller.org.dto.OrgPersonnelExportCondition;
import com.paladin.demo.model.org.OrgPersonnel;
import com.paladin.demo.service.org.OrgPersonnelService;
import com.paladin.demo.service.org.dto.OrgPersonnelQuery;
import com.paladin.demo.service.org.dto.OrgPersonnelDTO;
import com.paladin.demo.service.org.vo.OrgPersonnelVO;

import com.paladin.common.core.export.ExportUtil;
import com.paladin.common.core.ControllerSupport;
import com.paladin.framework.common.R;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.service.QueryInputMethod;
import com.paladin.framework.service.QueryOutputMethod;
import com.paladin.framework.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/demo/org/personnel")
public class OrgPersonnelController extends ControllerSupport {

    @Autowired
    private OrgPersonnelService orgPersonnelService;

    // @QueryInputMethod 用于查询回显
    @GetMapping("/index")
    @QueryInputMethod(queryClass = OrgPersonnelQuery.class)
    public String index() {
        return "/demo/org/org_personnel_index";
    }

    @RequestMapping(value = "/find/page", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    @QueryOutputMethod(queryClass = OrgPersonnelQuery.class, paramIndex = 0)
    public Object findPage(OrgPersonnelQuery query) {
        return R.success(orgPersonnelService.searchPage(query));
    }
    
    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {   	
        return R.success(beanCopy(orgPersonnelService.get(id), new OrgPersonnelVO()));
    }
    
    @GetMapping("/add")
    public String addInput() {
        return "/demo/org/org_personnel_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
    	model.addAttribute("id", id);
        return "/demo/org/org_personnel_detail";
    }
    
    @PostMapping("/save")
	@ResponseBody
    public Object save(@Valid OrgPersonnelDTO orgPersonnelDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
        OrgPersonnel model = beanCopy(orgPersonnelDTO, new OrgPersonnel());
		String id = UUIDUtil.createUUID();
		model.setId(id);
		if (orgPersonnelService.save(model) > 0) {
			return R.success(beanCopy(orgPersonnelService.get(id), new OrgPersonnelVO()));
		}
		return R.fail("保存失败");
	}

    @PostMapping("/update")
	@ResponseBody
    public Object update(@Valid OrgPersonnelDTO orgPersonnelDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
		String id = orgPersonnelDTO.getId();
		OrgPersonnel model = beanCopy(orgPersonnelDTO, orgPersonnelService.get(id));
		if (orgPersonnelService.update(model) > 0) {
			return R.success(beanCopy(orgPersonnelService.get(id), new OrgPersonnelVO()));
		}
		return R.fail("更新失败");
	}

    @RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object delete(@RequestParam String id) {
        return orgPersonnelService.removeByPrimaryKey(id) ? R.success() : R.fail("保存失败");
    }
    
    @PostMapping(value = "/export")
	@ResponseBody
	public Object export(@RequestBody OrgPersonnelExportCondition condition) {
		if (condition == null) {
            return R.fail("导出失败：请求参数异常");
		}
		condition.sortCellIndex();
		OrgPersonnelQuery query = condition.getQuery();
		try {
			if (query != null) {
				if (condition.isExportAll()) {
					return R.success(ExportUtil.export(condition, orgPersonnelService.searchAll(query), OrgPersonnel.class));
				} else if (condition.isExportPage()) {
					return R.success(ExportUtil.export(condition, orgPersonnelService.searchPage(query).getData(), OrgPersonnel.class));
				}
			}
            return R.fail("导出数据失败：请求参数错误");
		} catch (IOException | ExcelWriteException e) {
            return R.fail("导出数据失败：" + e.getMessage());
		}
	}
}
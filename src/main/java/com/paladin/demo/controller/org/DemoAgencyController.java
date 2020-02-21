package com.paladin.demo.controller.org;

import com.paladin.common.core.export.ExportUtil;
import com.paladin.demo.controller.org.dto.DemoAgencyExportCondition;
import com.paladin.demo.model.org.DemoAgency;
import com.paladin.demo.service.org.DemoAgencyService;
import com.paladin.demo.service.org.dto.DemoAgencyDTO;
import com.paladin.demo.service.org.dto.DemoAgencyQuery;
import com.paladin.demo.service.org.vo.DemoAgencyVO;
import com.paladin.framework.common.R;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.spring.annotation.QueryInputMethod;
import com.paladin.framework.spring.annotation.QueryOutputMethod;
import com.paladin.framework.utils.uuid.UUIDUtil;
import com.paladin.common.core.ControllerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/demo/demo/agency")
public class DemoAgencyController extends ControllerSupport {

    @Autowired
    private DemoAgencyService demoAgencyService;

    @GetMapping("/index")
    @QueryInputMethod(queryClass = DemoAgencyQuery.class)
    public String index() {
        return "/demo/org/demo_agency_index";
    }

    @RequestMapping(value = "/select/find", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object findSelect() {
        return R.success(demoAgencyService.searchPage(new DemoAgencyQuery()).getData());
    }

    @RequestMapping(value = "/find/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @QueryOutputMethod(queryClass = DemoAgencyQuery.class, paramIndex = 0)
    public Object findPage(DemoAgencyQuery query) {
        return R.success(demoAgencyService.searchPage(query));
    }

    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {
        return R.success(beanCopy(demoAgencyService.get(id), new DemoAgencyVO()));
    }

    @GetMapping("/add")
    public String addInput() {
        return "/demo/org/demo_agency_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
        model.addAttribute("id", id);
        return "/demo/org/demo_agency_detail";
    }

    @PostMapping("/save")
    @ResponseBody
    public Object save(@Valid DemoAgencyDTO demoAgencyDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validErrorHandler(bindingResult);
        }
        DemoAgency model = beanCopy(demoAgencyDTO, new DemoAgency());
        String id = UUIDUtil.createUUID();
        model.setId(id);
        if (demoAgencyService.save(model) > 0) {
            return R.success(beanCopy(demoAgencyService.get(id), new DemoAgencyVO()));
        }
        return R.fail("保存失败");
    }

    @PostMapping("/update")
    @ResponseBody
    public Object update(@Valid DemoAgencyDTO demoAgencyDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validErrorHandler(bindingResult);
        }
        String id = demoAgencyDTO.getId();
        DemoAgency model = beanCopy(demoAgencyDTO, demoAgencyService.get(id));
        if (demoAgencyService.update(model) > 0) {
            return R.success(beanCopy(demoAgencyService.get(id), new DemoAgencyVO()));
        }
        return R.fail("更新失败");
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object delete(@RequestParam String id) {
        return demoAgencyService.removeByPrimaryKey(id) ? R.success() : R.fail("保存失败");
    }

    @PostMapping(value = "/export")
    @ResponseBody
    public Object export(@RequestBody DemoAgencyExportCondition condition) {
        if (condition == null) {
            return R.fail("导出失败：请求参数异常");
        }
        condition.sortCellIndex();
        DemoAgencyQuery query = condition.getQuery();
        try {
            if (query != null) {
                if (condition.isExportAll()) {
                    return R.success(ExportUtil.export(condition, demoAgencyService.searchAll(query), DemoAgency.class));
                } else if (condition.isExportPage()) {
                    return R.success(ExportUtil.export(condition, demoAgencyService.searchPage(query).getData(), DemoAgency.class));
                }
            }
            return R.fail("导出数据失败：请求参数错误");
        } catch (IOException | ExcelWriteException e) {
            return R.fail("导出数据失败：" + e.getMessage());
        }
    }
}
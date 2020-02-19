package com.paladin.demo.controller.org;

import com.paladin.demo.controller.org.dto.DemoAgencyExportCondition;
import com.paladin.demo.model.org.DemoAgency;
import com.paladin.demo.service.org.DemoAgencyService;
import com.paladin.demo.service.org.dto.DemoAgencyQuery;
import com.paladin.demo.service.org.dto.DemoAgencyDTO;
import com.paladin.demo.service.org.vo.DemoAgencyVO;

import com.paladin.common.core.export.ExportUtil;
import com.paladin.framework.web.ControllerSupport;
import com.paladin.framework.spring.annotation.QueryInputMethod;
import com.paladin.framework.spring.annotation.QueryOutputMethod;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.web.response.CommonResponse;
import com.paladin.framework.utils.uuid.UUIDUtil;

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

import java.io.IOException;

import javax.validation.Valid;

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

	@RequestMapping(value = "/select/find", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object findSelect() {
		return CommonResponse.getSuccessResponse(demoAgencyService.searchPage(new DemoAgencyQuery()).getData());
	}

	@RequestMapping(value = "/find/page", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	@QueryOutputMethod(queryClass = DemoAgencyQuery.class, paramIndex = 0)
	public Object findPage(DemoAgencyQuery query) {
		return CommonResponse.getSuccessResponse(demoAgencyService.searchPage(query));
	}

	@GetMapping("/get")
	@ResponseBody
	public Object getDetail(@RequestParam String id, Model model) {
		return CommonResponse.getSuccessResponse(beanCopy(demoAgencyService.get(id), new DemoAgencyVO()));
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
			return CommonResponse.getSuccessResponse(beanCopy(demoAgencyService.get(id), new DemoAgencyVO()));
		}
		return CommonResponse.getFailResponse();
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
			return CommonResponse.getSuccessResponse(beanCopy(demoAgencyService.get(id), new DemoAgencyVO()));
		}
		return CommonResponse.getFailResponse();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object delete(@RequestParam String id) {
		return CommonResponse.getResponse(demoAgencyService.removeByPrimaryKey(id));
	}

	@PostMapping(value = "/export")
	@ResponseBody
	public Object export(@RequestBody DemoAgencyExportCondition condition) {
		if (condition == null) {
			return CommonResponse.getFailResponse("导出失败：请求参数异常");
		}
		condition.sortCellIndex();
		DemoAgencyQuery query = condition.getQuery();
		try {
			if (query != null) {
				if (condition.isExportAll()) {
					return CommonResponse.getSuccessResponse("success", ExportUtil.export(condition, demoAgencyService.searchAll(query), DemoAgency.class));
				} else if (condition.isExportPage()) {
					return CommonResponse.getSuccessResponse("success",
							ExportUtil.export(condition, demoAgencyService.searchPage(query).getData(), DemoAgency.class));
				}
			}
			return CommonResponse.getFailResponse("导出数据失败：请求参数错误");
		} catch (IOException | ExcelWriteException e) {
			return CommonResponse.getFailResponse("导出数据失败：" + e.getMessage());
		}
	}
}
package com.paladin.common.controller.syst;

import com.paladin.common.service.syst.SysLoggerLoginService;
import com.paladin.common.service.syst.dto.SysLoggerLoginQuery;
import com.paladin.common.service.syst.dto.SysLoggerLoginDTO;
import com.paladin.common.service.syst.vo.SysLoggerLoginVO;
import com.paladin.common.controller.syst.dto.SysLoggerLoginExportCondition;
import com.paladin.common.core.export.ExportUtil;
import com.paladin.common.model.syst.SysLoggerLogin;
import com.paladin.framework.core.ControllerSupport;
import com.paladin.framework.core.query.QueryInputMethod;
import com.paladin.framework.core.query.QueryOutputMethod;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.web.response.CommonResponse;
import com.paladin.framework.utils.uuid.UUIDUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

import javax.validation.Valid;

@Controller
@RequestMapping("/common/sys/logger/login")
public class SysLoggerLoginController extends ControllerSupport {

	@Autowired
	private SysLoggerLoginService sysLoggerLoginService;

	@GetMapping("/index")
	@QueryInputMethod(queryClass = SysLoggerLoginQuery.class)
	public String index() {
		return "/common/syst/sys_logger_login_index";
	}

	@RequestMapping(value = "/find/page", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	@QueryOutputMethod(queryClass = SysLoggerLoginQuery.class, paramIndex = 0)
	public Object findPage(SysLoggerLoginQuery query) {
		return CommonResponse.getSuccessResponse(sysLoggerLoginService.searchPage(query));
	}

	// @GetMapping("/get")
	// @ResponseBody
	public Object getDetail(@RequestParam String id, Model model) {
		return CommonResponse.getSuccessResponse(beanCopy(sysLoggerLoginService.get(id), new SysLoggerLoginVO()));
	}

	// @GetMapping("/add")
	public String addInput() {
		return "/common/syst/sys_logger_login_add";
	}

	// @GetMapping("/detail")
	public String detailInput(@RequestParam String id, Model model) {
		model.addAttribute("id", id);
		return "/common/syst/sys_logger_login_detail";
	}

	// @PostMapping("/save")
	// @ResponseBody
	public Object save(@Valid SysLoggerLoginDTO sysLoggerLoginDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
		SysLoggerLogin model = beanCopy(sysLoggerLoginDTO, new SysLoggerLogin());
		String id = UUIDUtil.createUUID();
		model.setId(id);
		if (sysLoggerLoginService.save(model) > 0) {
			return CommonResponse.getSuccessResponse(beanCopy(sysLoggerLoginService.get(id), new SysLoggerLoginVO()));
		}
		return CommonResponse.getFailResponse();
	}

	// @PostMapping("/update")
	// @ResponseBody
	public Object update(@Valid SysLoggerLoginDTO sysLoggerLoginDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return validErrorHandler(bindingResult);
		}
		String id = sysLoggerLoginDTO.getId();
		SysLoggerLogin model = beanCopy(sysLoggerLoginDTO, sysLoggerLoginService.get(id));
		if (sysLoggerLoginService.update(model) > 0) {
			return CommonResponse.getSuccessResponse(beanCopy(sysLoggerLoginService.get(id), new SysLoggerLoginVO()));
		}
		return CommonResponse.getFailResponse();
	}

	// @RequestMapping(value = "/delete", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// @ResponseBody
	public Object delete(@RequestParam String id) {
		return CommonResponse.getResponse(sysLoggerLoginService.removeByPrimaryKey(id));
	}

	// @PostMapping(value = "/export")
	// @ResponseBody
	public Object export(@RequestBody SysLoggerLoginExportCondition condition) {
		if (condition == null) {
			return CommonResponse.getFailResponse("导出失败：请求参数异常");
		}
		condition.sortCellIndex();
		SysLoggerLoginQuery query = condition.getQuery();
		try {
			if (query != null) {
				if (condition.isExportAll()) {
					return CommonResponse.getSuccessResponse("success",
							ExportUtil.export(condition, sysLoggerLoginService.searchAll(query), SysLoggerLogin.class));
				} else if (condition.isExportPage()) {
					return CommonResponse.getSuccessResponse("success",
							ExportUtil.export(condition, sysLoggerLoginService.searchPage(query).getData(), SysLoggerLogin.class));
				}
			}
			return CommonResponse.getFailResponse("导出数据失败：请求参数错误");
		} catch (IOException | ExcelWriteException e) {
			return CommonResponse.getFailResponse("导出数据失败：" + e.getMessage());
		}
	}
}
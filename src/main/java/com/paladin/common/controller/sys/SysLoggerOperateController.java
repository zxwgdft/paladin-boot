package com.paladin.common.controller.sys;

import com.paladin.common.controller.sys.dto.SysLoggerOperateExportCondition;
import com.paladin.common.model.sys.SysLoggerOperate;
import com.paladin.common.service.sys.SysLoggerOperateService;
import com.paladin.common.service.sys.dto.SysLoggerOperateQuery;
import com.paladin.common.service.sys.dto.SysLoggerOperateDTO;
import com.paladin.common.service.sys.vo.SysLoggerOperateVO;

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
@RequestMapping("/common/sys/logger/operate")
public class SysLoggerOperateController extends ControllerSupport {

    @Autowired
    private SysLoggerOperateService sysLoggerOperateService;


    @GetMapping("/index")
    public String index() {
        return "/common/sys/sys_logger_operate_index";
    }

    @RequestMapping(value = "/find/page", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object findPage(SysLoggerOperateQuery query) {
        return R.success(sysLoggerOperateService.searchPage(query, SysLoggerOperateVO.class));
    }
    
    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id) {
        return R.success(sysLoggerOperateService.get(id, SysLoggerOperateVO.class));
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
    	model.addAttribute("id", id);
        return "/common/sys/sys_logger_operate_detail";
    }

    @PostMapping(value = "/export")
	@ResponseBody
	public Object export(@RequestBody SysLoggerOperateExportCondition condition) {
		if (condition == null) {
            return R.fail("导出失败：请求参数异常");
		}
		condition.sortCellIndex();
		SysLoggerOperateQuery query = condition.getQuery();
		try {
			if (query != null) {
				if (condition.isExportAll()) {
					return R.success(ExportUtil.export(condition, sysLoggerOperateService.searchAll(query), SysLoggerOperate.class));
				} else if (condition.isExportPage()) {
					return R.success(ExportUtil.export(condition, sysLoggerOperateService.searchPage(query).getData(), SysLoggerOperate.class));
				}
			}
            return R.fail("导出数据失败：请求参数错误");
		} catch (IOException | ExcelWriteException e) {
            return R.fail("导出数据失败：" + e.getMessage());
		}
	}
}
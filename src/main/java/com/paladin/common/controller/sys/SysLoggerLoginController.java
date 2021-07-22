package com.paladin.common.controller.sys;

import com.paladin.common.controller.sys.dto.SysLoggerLoginExportCondition;
import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.export.ExportUtil;
import com.paladin.common.model.sys.SysLoggerLogin;
import com.paladin.common.service.sys.SysLoggerLoginService;
import com.paladin.common.service.sys.dto.SysLoggerLoginQuery;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.PageResult;
import com.paladin.framework.service.annotation.QueryInputMethod;
import com.paladin.framework.service.annotation.QueryOutputMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/common/sys/logger/login")
public class SysLoggerLoginController extends ControllerSupport {

    @Autowired
    private SysLoggerLoginService sysLoggerLoginService;

    @GetMapping("/index")
    @QueryInputMethod(queryClass = SysLoggerLoginQuery.class)
    public String index() {
        return "/common/sys/sys_logger_login_index";
    }

    @PostMapping(value = "/find/page")
    @ResponseBody
    @QueryOutputMethod(queryClass = SysLoggerLoginQuery.class, paramIndex = 0)
    public PageResult<SysLoggerLogin> findPage(SysLoggerLoginQuery query) {
        return sysLoggerLoginService.findPage(query);
    }

    @PostMapping(value = "/export")
    @ResponseBody
    public String export(@RequestBody SysLoggerLoginExportCondition condition) {
        if (condition == null) {
            throw new BusinessException("导出失败：请求参数异常");
        }
        SysLoggerLoginQuery query = condition.getQuery();
        try {
            if (query != null) {
                if (condition.isExportAll()) {
                    return ExportUtil.export(condition, sysLoggerLoginService.findList(query), SysLoggerLogin.class);
                } else if (condition.isExportPage()) {
                    return ExportUtil.export(condition, sysLoggerLoginService.findPage(query).getData(), SysLoggerLogin.class);
                }
            }
            throw new BusinessException("导出数据失败：请求参数错误");
        } catch (IOException | ExcelWriteException e) {
            throw new BusinessException("导出数据失败", e);
        }
    }
}
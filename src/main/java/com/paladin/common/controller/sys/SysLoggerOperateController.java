package com.paladin.common.controller.sys;

import com.paladin.common.controller.sys.dto.SysLoggerOperateExportCondition;
import com.paladin.common.core.CommonUserSession;
import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.export.ExportUtil;
import com.paladin.common.core.security.NeedRoleLevel;
import com.paladin.common.model.sys.SysLoggerOperate;
import com.paladin.common.service.sys.SysLoggerOperateService;
import com.paladin.common.service.sys.dto.SysLoggerOperateQuery;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/find/page")
    @ResponseBody
    @NeedRoleLevel(CommonUserSession.ROLE_LEVEL_APP_ADMIN)
    public PageResult<SysLoggerOperate> findPage(SysLoggerOperateQuery query) {
        return sysLoggerOperateService.findPage(query);
    }
    
    @PostMapping(value = "/export")
    @ResponseBody
    @NeedRoleLevel(CommonUserSession.ROLE_LEVEL_APP_ADMIN)
    public String export(@RequestBody SysLoggerOperateExportCondition condition) {
        if (condition == null) {
            throw new BusinessException("导出失败：请求参数异常");
        }
        SysLoggerOperateQuery query = condition.getQuery();
        try {
            if (query != null) {
                if (condition.isExportAll()) {
                    return ExportUtil.export(condition, sysLoggerOperateService.findList(query), SysLoggerOperate.class);
                } else if (condition.isExportPage()) {
                    return ExportUtil.export(condition, sysLoggerOperateService.findPage(query).getData(), SysLoggerOperate.class);
                }
            }
            throw new BusinessException("导出数据失败：请求参数错误");
        } catch (IOException | ExcelWriteException e) {
            throw new BusinessException("导出数据失败", e);
        }
    }
}
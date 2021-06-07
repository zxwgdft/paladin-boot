package com.paladin.common.controller.sys;

import com.paladin.common.controller.sys.dto.SysLoggerLoginExportCondition;
import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.export.ExportUtil;
import com.paladin.common.model.sys.SysLoggerLogin;
import com.paladin.common.service.sys.SysLoggerLoginService;
import com.paladin.common.service.sys.dto.SysLoggerLoginQuery;
import com.paladin.framework.api.R;
import com.paladin.framework.excel.write.ExcelWriteException;
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

    @RequestMapping(value = "/find/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @QueryOutputMethod(queryClass = SysLoggerLoginQuery.class, paramIndex = 0)
    public PageResult<SysLoggerLogin> findPage(SysLoggerLoginQuery query) {
        return sysLoggerLoginService.findPage(query);
    }

    @PostMapping(value = "/export")
    @ResponseBody
    public Object export(@RequestBody SysLoggerLoginExportCondition condition) {
        if (condition == null) {
            return R.fail("导出失败：请求参数异常");
        }
        condition.sortCellIndex();
        SysLoggerLoginQuery query = condition.getQuery();
        try {
            if (query != null) {
                if (condition.isExportAll()) {
                    return R.success(ExportUtil.export(condition, sysLoggerLoginService.findList(query), SysLoggerLogin.class));
                } else if (condition.isExportPage()) {
                    return R.success(ExportUtil.export(condition, sysLoggerLoginService.findPage(query).getData(), SysLoggerLogin.class));
                }
            }
            return R.fail("导出数据失败：请求参数错误");
        } catch (IOException | ExcelWriteException e) {
            return R.fail("导出数据失败：" + e.getMessage());
        }
    }
}
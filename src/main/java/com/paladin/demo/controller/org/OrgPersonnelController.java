package com.paladin.demo.controller.org;

import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.export.ExportUtil;
import com.paladin.common.core.log.OperationLog;
import com.paladin.common.core.security.NeedPermission;
import com.paladin.demo.controller.org.dto.OrgPersonnelExportCondition;
import com.paladin.demo.model.org.OrgPersonnel;
import com.paladin.demo.service.org.OrgPersonnelService;
import com.paladin.demo.service.org.dto.OrgPersonnelDTO;
import com.paladin.demo.service.org.dto.OrgPersonnelQuery;
import com.paladin.demo.service.org.vo.OrgPersonnelVO;
import com.paladin.framework.api.R;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.excel.write.ValueFormatter;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.PageResult;
import com.paladin.framework.service.annotation.QueryInputMethod;
import com.paladin.framework.service.annotation.QueryOutputMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/demo/org/personnel")
public class OrgPersonnelController extends ControllerSupport {

    @Autowired
    private OrgPersonnelService orgPersonnelService;


    // @QueryInputMethod 用于查询回显，加载页面时会尝试去读取query参数
    @GetMapping("/index")
    @QueryInputMethod(queryClass = OrgPersonnelQuery.class)
    public String index() {
        return "/demo/org/org_personnel_index";
    }

    // @QueryOutputMethod 用于查询回显，查询动作后会尝试把查询参数保存在session中
    @PostMapping(value = "/find/page")
    @ResponseBody
    @QueryOutputMethod(queryClass = OrgPersonnelQuery.class, paramIndex = 0)
    public PageResult<OrgPersonnelVO> findPage(OrgPersonnelQuery query) {
        return orgPersonnelService.findPersonnelPage(query);
    }

    // 获取详细数据
    @GetMapping("/get")
    @ResponseBody
    public OrgPersonnel getDetail(@RequestParam String id) {
        return orgPersonnelService.get(id);
    }

    // 新增页面
    @GetMapping("/add")
    public String addInput() {
        return "/demo/org/org_personnel_add";
    }

    // 详情页面
    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
        model.addAttribute("id", id);
        return "/demo/org/org_personnel_detail";
    }

    // 编辑页面
    @GetMapping("/edit")
    public String editInput(@RequestParam String id, Model model) {
        model.addAttribute("id", id);
        return "/demo/org/org_personnel_edit";
    }

    // 新增人员，OrgPersonnelDTO限制新增的字段，OrgPersonnelDTO中应该只存在可以新增和必要的id等字段，如果冲突可与update方法不共用一个DTO
    @PostMapping("/save")
    @ResponseBody
    @OperationLog(model = "人员管理", operate = "人员新增")
    @NeedPermission("org:personnel:save")
    public R save(@Valid OrgPersonnelDTO orgPersonnelDTO, BindingResult bindingResult) {
        // 返回固定格式校验错误数据，用于展示
        validErrorHandler(bindingResult);
        String password = orgPersonnelService.savePersonnel(orgPersonnelDTO);
        // 保存成功后直接返回完整对象
        return R.success(password);
    }

    // 更新人员，OrgPersonnelDTO限制新增的字段，OrgPersonnelDTO中应该只存在可以新增和必要的id等字段，如果冲突可与update方法不共用一个DTO
    @PostMapping("/update")
    @ResponseBody
    @OperationLog(model = "人员管理", operate = "人员更新")
    @NeedPermission("org:personnel:update")        // 自定义权限方式，直接判断权限code是否相等
    public R update(@Valid OrgPersonnelDTO orgPersonnelDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        orgPersonnelService.updatePersonnel(orgPersonnelDTO);
        // 更新成功后直接返回完整对象
        return R.SUCCESS;
    }

    // 删除数据
    @PostMapping(value = "/delete")
    @ResponseBody
    @OperationLog(model = "人员管理", operate = "人员删除")
    @NeedPermission("org:personnel:delete")
    public R delete(@RequestParam String id) {
        // 根据主键删除
        orgPersonnelService.removePersonnel(id);
        return R.SUCCESS;
    }

    // 导出Excel
    @PostMapping(value = "/export")
    @ResponseBody
    @NeedPermission("org:personnel:export")
    public String export(@RequestBody OrgPersonnelExportCondition condition) {
        if (condition == null) {
            throw new BusinessException("导出失败：请求参数异常");
        }
        // 用于特殊转换，如果不需要则不需要设置
        condition.setExcelValueFormatMap(excelValueFormatMap);
        OrgPersonnelQuery query = condition.getQuery();
        try {
            if (query != null) {
                if (condition.isExportAll()) {
                    return ExportUtil.export(condition, orgPersonnelService.findPersonnelList(query), OrgPersonnelVO.class);
                } else if (condition.isExportPage()) {
                    return ExportUtil.export(condition, orgPersonnelService.findPersonnelPage(query).getData(), OrgPersonnelVO.class);
                }
            }
            throw new BusinessException("导出数据失败：请求参数错误");
        } catch (IOException | ExcelWriteException e) {
            throw new BusinessException("导出数据失败", e);
        }
    }

    // EXCEL自定义导出列
    private static Map<String, ValueFormatter> excelValueFormatMap;

    static {
        excelValueFormatMap = new HashMap<>();
        // 自定义导出excel格式化方法，隐藏身份证中间部分
        excelValueFormatMap.put(OrgPersonnel.FIELD_IDENTIFICATION_NO, (obj) -> {
            // obj会根据字段名称获取相应值
            if (obj != null) {
                String val = (String) obj;
                int l = val.length();
                return l > 8 ? val.substring(0, 4) + "******************" + val.substring(l - 4) : val;
            }
            return "";
        });
    }
}
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
import com.paladin.framework.excel.write.CellStyleCreator;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.excel.write.ValueFormatter;
import com.paladin.framework.excel.write.WriteColumn;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.PageResult;
import com.paladin.framework.service.annotation.QueryInputMethod;
import com.paladin.framework.service.annotation.QueryOutputMethod;
import com.paladin.framework.utils.reflect.LambdaUtil;
import org.apache.poi.ss.usermodel.*;
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
        // getWhole 可以获取整个对象属性，包括注解@TableField(select = false)的
        // 当然也可以结合实际情况自己写sql或者去除该注解
        return orgPersonnelService.getWhole(id);
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
        return R.SUCCESS;
    }

    // 删除数据
    @PostMapping(value = "/delete")
    @ResponseBody
    @OperationLog(model = "人员管理", operate = "人员删除")
    @NeedPermission("org:personnel:delete")
    public R delete(@RequestParam String id) {
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
        condition.setValueFormatMap(valueFormatMap);
        // 添加一个性别字段的样式案例
        condition.putCellStyleCreator(LambdaUtil.getFieldNameByFunction(OrgPersonnelVO::getSex), sexCellStyleCreator);

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
    private static Map<String, ValueFormatter> valueFormatMap;
    private static CellStyleCreator sexCellStyleCreator = new CellStyleCreator() {
        @Override
        public CellStyle createCellStyle(Workbook workbook, CellStyle commonCellStyle, WriteColumn writeColumn, Object data) {
            CellStyle style = workbook.createCellStyle();
            if (commonCellStyle != null) {
                style.cloneStyleFrom(commonCellStyle);
            }

            style.setWrapText(true);
            style.setAlignment(HorizontalAlignment.CENTER);

            Integer sex = (Integer) data;
            if (sex != null) {
                short color = sex.intValue() == 1 ? IndexedColors.LIGHT_BLUE.index : IndexedColors.LIGHT_ORANGE.index;
                style.setFillForegroundColor(color);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                Font font = workbook.createFont();
                font.setBold(true);
                font.setColor(IndexedColors.WHITE.index);

                style.setFont(font);
            }
            return style;
        }

        public boolean isStyleUniversal() {
            return false;
        }
    };

    static {
        valueFormatMap = new HashMap<>();
        // 自定义导出excel格式化方法，隐藏身份证中间部分
        valueFormatMap.put(LambdaUtil.getFieldNameByFunction(OrgPersonnelVO::getIdentificationNo), (obj) -> {
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
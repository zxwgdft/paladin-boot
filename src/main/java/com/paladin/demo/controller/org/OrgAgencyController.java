package com.paladin.demo.controller.org;

import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.common.core.export.ExportUtil;
import com.paladin.common.core.log.OperationLog;
import com.paladin.common.core.security.NeedPermission;
import com.paladin.demo.controller.org.dto.OrgAgencyExportCondition;
import com.paladin.demo.controller.org.dto.OrgAgencyQuery;
import com.paladin.demo.model.org.OrgAgency;
import com.paladin.demo.service.org.OrgAgencyContainer;
import com.paladin.demo.service.org.OrgAgencyService;
import com.paladin.demo.service.org.dto.OrgAgencyDTO;
import com.paladin.framework.api.R;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.excel.write.ValueFormatter;
import com.paladin.framework.excel.write.WriteColumn;
import com.paladin.framework.exception.BusinessException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/demo/org/agency")
public class OrgAgencyController extends ControllerSupport {

    @Autowired
    private OrgAgencyService orgAgencyService;

    @GetMapping("/index")
    public String index() {
        return "/demo/org/org_agency_index";
    }

    @PostMapping(value = "/find")
    @ResponseBody
    public List<OrgAgency> findPage(OrgAgencyQuery query) {
        return orgAgencyService.findList(query);
    }

    // 获取树形结构所有单位信息
    @GetMapping("/find/tree")
    @ResponseBody
    public List<OrgAgencyContainer.Agency> findTree() {
        return DataCacheHelper.getData(OrgAgencyContainer.class).getAgencyTree();
    }

    @GetMapping("/get")
    @ResponseBody
    public OrgAgency getDetail(@RequestParam int id) {
        return orgAgencyService.get(id);
    }

    @GetMapping("/add")
    public String addInput() {
        return "/demo/org/org_agency_add";
    }

    @GetMapping("/edit")
    public String detailInput(@RequestParam int id, Model model) {
        model.addAttribute("id", id);
        return "/demo/org/org_agency_edit";
    }

    @PostMapping("/save")
    @ResponseBody
    @NeedPermission("org:agency:save")
    @OperationLog(model = "机构管理", operate = "机构新增")
    public R save(@Valid OrgAgencyDTO orgagencyDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        orgAgencyService.saveAgency(orgagencyDTO);
        return R.SUCCESS;
    }

    @PostMapping("/update")
    @ResponseBody
    @NeedPermission("org:agency:update")
    @OperationLog(model = "机构管理", operate = "机构更新")
    public R update(@Valid OrgAgencyDTO orgagencyDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        orgAgencyService.updateAgency(orgagencyDTO);
        return R.SUCCESS;
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    @NeedPermission("org:agency:delete")
    @OperationLog(model = "机构管理", operate = "机构删除")
    public R delete(@RequestParam int id) {
        orgAgencyService.removeAgency(id);
        return R.SUCCESS;
    }


    // 导出Excel
    @PostMapping(value = "/export")
    @ResponseBody
    @NeedPermission("org:agency:export")
    public String export(@RequestBody OrgAgencyExportCondition condition) {
        if (condition == null) {
            throw new BusinessException("导出失败：请求参数异常");
        }

        condition.putValueFormatter(OrgAgency::getId, new ValueFormatter() {
            private OrgAgencyContainer container;

            @Override
            public String format(Object obj) {
                if (container == null) {
                    container = DataCacheHelper.getData(OrgAgencyContainer.class);
                }

                Integer id = (Integer) obj;
                OrgAgencyContainer.Agency agency = container.getAgency(id);
                return agency == null ? "" : agency.getFullName();
            }
        });

        condition.putCellStyleCreator(
                OrgAgency::getId,
                (Workbook workbook, CellStyle commonCellStyle, WriteColumn writeColumn, Object data) -> {
                    CellStyle style = workbook.createCellStyle();
                    if (commonCellStyle != null) {
                        style.cloneStyleFrom(commonCellStyle);
                    }
                    style.setWrapText(true);
                    style.setAlignment(HorizontalAlignment.LEFT);
                    style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                    Font font = workbook.createFont();
                    font.setBold(true);
                    font.setColor(IndexedColors.WHITE.index);

                    style.setFont(font);

                    return style;
                });

        OrgAgencyQuery query = condition.getQuery();
        try {
            if (query != null) {
                return ExportUtil.export(condition, orgAgencyService.findList(query), OrgAgency.class);
            }
            throw new BusinessException("导出数据失败：请求参数错误");
        } catch (IOException | ExcelWriteException e) {
            throw new BusinessException("导出数据失败", e);
        }
    }

}
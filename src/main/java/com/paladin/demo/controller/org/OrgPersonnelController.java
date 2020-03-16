package com.paladin.demo.controller.org;

import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.export.ExportUtil;
import com.paladin.demo.controller.org.dto.OrgPersonnelExportCondition;
import com.paladin.demo.model.org.OrgPersonnel;
import com.paladin.demo.service.org.OrgPersonnelService;
import com.paladin.demo.service.org.dto.OrgPersonnelDTO;
import com.paladin.demo.service.org.dto.OrgPersonnelQuery;
import com.paladin.demo.service.org.vo.OrgPersonnelVO;
import com.paladin.framework.common.R;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.excel.write.ValueFormator;
import com.paladin.framework.service.QueryInputMethod;
import com.paladin.framework.service.QueryOutputMethod;
import com.paladin.framework.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @RequestMapping(value = "/find/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @QueryOutputMethod(queryClass = OrgPersonnelQuery.class, paramIndex = 0)
    public Object findPage(OrgPersonnelQuery query) {
        // OrgPersonnel应尽量与数据表对应，不做扩展，并且可能带有一些系统自带自带，所以应该有专门的VO对象用于返回数据
        // 当然为了效率与简单，也可以直接返回OrgPersonnel对象，一般并不会有太大问题
        return R.success(orgPersonnelService.searchPage(query, OrgPersonnelVO.class));
    }

    // 获取详细数据
    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id) {
        return R.success(orgPersonnelService.get(id, OrgPersonnelVO.class));
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

    // 新增人员，OrgPersonnelDTO限制新增的字段，OrgPersonnelDTO中应该只存在可以新增和必要的id等字段，如果冲突可与update方法不共用一个DTO
    @PostMapping("/save")
    @ResponseBody
    public Object save(@Valid OrgPersonnelDTO orgPersonnelDTO, BindingResult bindingResult, @RequestParam(required = false) MultipartFile[] attachmentFiles) {
        if (bindingResult.hasErrors()) {
            // 返回固定格式校验错误数据，用于展示
            return validErrorHandler(bindingResult);
        }

        String id = UUIDUtil.createUUID();
        orgPersonnelDTO.setId(id);

        if (orgPersonnelService.savePersonnel(orgPersonnelDTO)) {
            // 保存成功后直接返回完整对象
            return R.success(orgPersonnelService.get(id, OrgPersonnelVO.class));
        }
        return R.fail("保存失败");
    }

    // 更新人员，OrgPersonnelDTO限制新增的字段，OrgPersonnelDTO中应该只存在可以新增和必要的id等字段，如果冲突可与update方法不共用一个DTO
    @PostMapping("/update")
    @ResponseBody
    public Object update(@Valid OrgPersonnelDTO orgPersonnelDTO, BindingResult bindingResult, @RequestParam(required = false) MultipartFile[] attachmentFiles) {
        if (bindingResult.hasErrors()) {
            // 返回固定格式校验错误数据，用于展示
            return validErrorHandler(bindingResult);
        }

        String id = orgPersonnelDTO.getId();
        if (orgPersonnelService.updatePersonnel(orgPersonnelDTO)) {
            // 更新成功后直接返回完整对象
            return R.success(orgPersonnelService.get(id, OrgPersonnelVO.class));
        }
        return R.fail("更新失败");
    }

    // 删除数据
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object delete(@RequestParam String id) {
        // 根据主键删除
        return orgPersonnelService.removeByPrimaryKey(id) ? R.success() : R.fail("保存失败");
    }

    // 导出Excel
    @PostMapping(value = "/export")
    @ResponseBody
    public Object export(@RequestBody OrgPersonnelExportCondition condition) {
        if (condition == null) {
            return R.fail("导出失败：请求参数异常");
        }
        // 用于特殊转换，如果不需要则不需要设置
        condition.setExcelValueFormatMap(excelValueFormatMap);
        condition.sortCellIndex();
        OrgPersonnelQuery query = condition.getQuery();
        try {
            if (query != null) {
                if (condition.isExportAll()) {
                    // orgPersonnelService.searchAll(query) 也可以替换为 orgPersonnelService.searchPage(query, OrgPersonnelVO.class)，从而在VO类中做一定处理
                    return R.success(ExportUtil.export(condition, orgPersonnelService.searchAll(OrgPersonnelVO.class, query), OrgPersonnelVO.class));
                } else if (condition.isExportPage()) {
                    return R.success(ExportUtil.export(condition, orgPersonnelService.searchPage(query, OrgPersonnelVO.class).getData(), OrgPersonnelVO.class));
                }
            }
            return R.fail("导出数据失败：请求参数错误");
        } catch (IOException | ExcelWriteException e) {
            return R.fail("导出数据失败：" + e.getMessage());
        }
    }

    // EXCEL自定义导出列
    private static Map<String, ValueFormator> excelValueFormatMap;

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
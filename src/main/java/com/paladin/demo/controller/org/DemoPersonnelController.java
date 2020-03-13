package com.paladin.demo.controller.org;

import com.paladin.common.core.ControllerSupport;
import com.paladin.common.core.export.ExportUtil;
import com.paladin.common.model.sys.SysAttachment;
import com.paladin.common.service.sys.SysAttachmentService;
import com.paladin.demo.controller.org.dto.DemoPersonnelExportCondition;
import com.paladin.demo.model.org.DemoPersonnel;
import com.paladin.demo.service.org.DemoPersonnelService;
import com.paladin.demo.service.org.dto.DemoPersonnelDTO;
import com.paladin.demo.service.org.dto.DemoPersonnelQuery;
import com.paladin.demo.service.org.vo.DemoPersonnelVO;
import com.paladin.framework.common.HttpCode;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/demo/demo/personnel")
public class DemoPersonnelController extends ControllerSupport {

    @Autowired
    private DemoPersonnelService demoPersonnelService;

    @Autowired
    private SysAttachmentService attachmentService;

    // @QueryInputMethod 用于查询回显
    @GetMapping("/index")
    @QueryInputMethod(queryClass = DemoPersonnelQuery.class)
    public String index() {
        return "/demo/org/demo_personnel_index";
    }

    // @QueryOutputMethod 用于查询回显
    @RequestMapping(value = "/find/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @QueryOutputMethod(queryClass = DemoPersonnelQuery.class, paramIndex = 0)
    public Object findPage(DemoPersonnelQuery query) {
        return R.success(demoPersonnelService.searchPage(query));
    }

    @GetMapping("/get")
    @ResponseBody
    public Object getDetail(@RequestParam String id, Model model) {
        return R.success(beanCopy(demoPersonnelService.get(id), new DemoPersonnelVO()));
    }

    @GetMapping("/add")
    public String addInput() {
        return "/demo/org/demo_personnel_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
        model.addAttribute("id", id);
        return "/demo/org/demo_personnel_detail";
    }

    // 新增DemoPersonnelDTO限制新增的字段，DemoPersonnelDTO中应该只存在可以新增和必要的id等字段，如果冲突可与update方法不共用一个DTO
    @PostMapping("/save")
    @ResponseBody
    public Object save(@Valid DemoPersonnelDTO demoPersonnelDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validErrorHandler(bindingResult);
        }

        List<SysAttachment> attachments = attachmentService.mergeAttachments(demoPersonnelDTO.getProfilePhoto(), demoPersonnelDTO.getProfilePhotoFile());
        if (attachments != null && attachments.size() > 1) {
            return R.fail(HttpCode.BAD_REQUEST, "附件数量不能超过1张");
        }
        demoPersonnelDTO.setProfilePhoto(attachmentService.splicingAttachmentId(attachments));

        DemoPersonnel model = beanCopy(demoPersonnelDTO, new DemoPersonnel());
        String id = UUIDUtil.createUUID();
        model.setId(id);
        if (demoPersonnelService.save(model)) {
            return R.success(beanCopy(demoPersonnelService.get(id), new DemoPersonnelVO()));
        }
        return R.fail("保存失败");
    }

    // 更新DemoPersonnelDTO限制修改的字段，DemoPersonnelDTO中应该只存在可以修改和必要的id等字段，如果冲突可与save方法不共用一个DTO
    @PostMapping("/update")
    @ResponseBody
    public Object update(@Valid DemoPersonnelDTO demoPersonnelDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validErrorHandler(bindingResult);
        }
        String id = demoPersonnelDTO.getId();
        DemoPersonnel model = demoPersonnelService.get(id);
        if (model == null) {
            return R.fail(HttpCode.BAD_REQUEST, "找不到需要修改的对象");
        }

        List<SysAttachment> attachments = attachmentService.replaceAndMergeAttachments(model.getProfilePhoto(), demoPersonnelDTO.getProfilePhoto(), demoPersonnelDTO.getProfilePhotoFile());
        if (attachments != null && attachments.size() > 1) {
            return R.fail(HttpCode.BAD_REQUEST, "附件数量不能超过1张");
        }
        demoPersonnelDTO.setProfilePhoto(attachmentService.splicingAttachmentId(attachments));

        model = beanCopy(demoPersonnelDTO, model);
        if (demoPersonnelService.update(model)) {
            return R.success(beanCopy(demoPersonnelService.get(id), new DemoPersonnelVO()));
        }
        return R.fail("更新失败");
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object delete(@RequestParam String id) {
        return demoPersonnelService.removeByPrimaryKey(id) ? R.success() : R.fail("保存失败");

    }

    // 导出excel
    @PostMapping(value = "/export")
    @ResponseBody
    public Object export(@RequestBody DemoPersonnelExportCondition condition) {
        if (condition == null) {
            return R.fail("导出失败：请求参数异常");
        }
        // 用于特殊转换，如果不需要则不需要设置
        condition.setExcelValueFormatMap(excelValueFormatMap);
        condition.sortCellIndex();
        DemoPersonnelQuery query = condition.getQuery();
        try {
            if (query != null) {
                if (condition.isExportAll()) {
                    return R.success(ExportUtil.export(condition, demoPersonnelService.searchAll(query), DemoPersonnel.class));
                } else if (condition.isExportPage()) {
                    return R.success(ExportUtil.export(condition, demoPersonnelService.searchPage(query).getData(), DemoPersonnel.class));
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
        excelValueFormatMap.put(DemoPersonnel.FIELD_IDENTIFICATIONNO, new ValueFormator() {
            @Override
            public String format(Object obj) {
                if (obj != null) {
                    String val = (String) obj;
                    int l = val.length();
                    return l > 8 ? val.substring(0, 4) + "******************" + val.substring(l - 4) : val;
                }
                return "";
            }
        });

    }
}
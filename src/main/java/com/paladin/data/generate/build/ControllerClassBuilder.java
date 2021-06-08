package com.paladin.data.generate.build;

import com.paladin.data.generate.GenerateBuilderContainer;
import com.paladin.data.generate.GenerateColumnOption;
import com.paladin.data.generate.GenerateTableOption;
import com.paladin.framework.constants.CommonConstants;
import com.paladin.framework.utils.reflect.NameUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

@Component
public class ControllerClassBuilder extends SpringBootClassBuilder {

    private static Template template = FreemarkerUtil.getTemplate("/template_controller.temp");

    public String buildContent(GenerateTableOption tableOption) {
        StringWriter writer = new StringWriter();


        HashMap<String, Object> params = new HashMap<>();

        params.put("package", getClassPackage(tableOption));

        StringBuilder sb = new StringBuilder();

        sb.append("import ").append(GenerateBuilderContainer.getClassImportPackage(BuilderType.EXPORT_QUERY_DTO, tableOption)).append(";\n");
        sb.append("import ").append(GenerateBuilderContainer.getClassImportPackage(BuilderType.MODEL, tableOption)).append(";\n");
        sb.append("import ").append(GenerateBuilderContainer.getClassImportPackage(BuilderType.SERVICE, tableOption)).append(";\n");
        sb.append("import ").append(GenerateBuilderContainer.getClassImportPackage(BuilderType.QUERY_DTO, tableOption)).append(";\n");
        sb.append("import ").append(GenerateBuilderContainer.getClassImportPackage(BuilderType.MODEL_DTO, tableOption)).append(";\n");

//        for (GenerateColumnOption columnOption : tableOption.getColumnOptions()) {
//            Integer isAtt = columnOption.getBuildColumnOption().getIsAttachment();
//            // 这里只处理一个附件字段，如果有多个，需要改为列表或者手动修改生成的代码
//            if (isAtt != null && isAtt == CommonConstants.YES) {
//                params.put("attachmentField", NameUtil.firstUpperCase(columnOption.getFieldName()));
//
//                Integer count = columnOption.getBuildColumnOption().getAttachmentCount();
//                if (count == null || count < 0) {
//                    count = 5;
//                }
//
//                params.put("attachmentSize", count);
//
//                sb.append("import com.paladin.framework.exception.BusinessException;\n");
//                sb.append("import com.paladin.common.model.sys.SysAttachment;\n");
//                sb.append("import com.paladin.common.service.sys.SysAttachmentService;\n");
//                sb.append("import java.util.List;\n");
//
//                break;
//            }
//        }

        params.put("imports", sb.toString());

        params.put("upperModelName", tableOption.getModelName());
        params.put("lowerModelName", NameUtil.firstLowerCase(tableOption.getModelName()));
        params.put("primaryName", NameUtil.firstUpperCase(tableOption.getFirstPrimaryName()));

        params.put("baseRequestMapping", getBaseRequestMapping(tableOption));

        params.put("indexRM", getIndexRequestMapping(tableOption));
        params.put("findPageRM", getFindPageRequestMapping(tableOption));
        params.put("addRM", getAddRequestMapping(tableOption));
        params.put("detailRM", getDetailRequestMapping(tableOption));
        params.put("getDetailRM", getGetDetailRequestMapping(tableOption));
        params.put("editRM", getEditRequestMapping(tableOption));
        params.put("saveRM", getSaveRequestMapping(tableOption));
        params.put("updateRM", getUpdateRequestMapping(tableOption));
        params.put("deleteRM", getDeleteRequestMapping(tableOption));
        params.put("exportRM", getExportRequestMapping(tableOption));

        params.put("indexPage", GenerateBuilderContainer.getViewPath(BuilderType.PAGE_INDEX, tableOption));
        params.put("addPage", GenerateBuilderContainer.getViewPath(BuilderType.PAGE_ADD, tableOption));
        params.put("detailPage", GenerateBuilderContainer.getViewPath(BuilderType.PAGE_DETAIL, tableOption));
        params.put("editPage", GenerateBuilderContainer.getViewPath(BuilderType.PAGE_EDIT, tableOption));

        try {
            template.process(params, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException("生成文档失败", e);
        }
        return writer.toString();
    }

    public String getBaseRequestMapping(GenerateTableOption tableOption) {
        String table = tableOption.getTable().getName();
        String path = "/" + tableOption.getModel() + "/" + table.replaceAll("_", "/");
        return path;
    }

    public String getIndexRequestMapping(GenerateTableOption tableOption) {
        return "/index";
    }

    public String getFindPageRequestMapping(GenerateTableOption tableOption) {
        return "/find/page";
    }

    public String getExportRequestMapping(GenerateTableOption tableOption) {
        return "/export";
    }

    public String getAddRequestMapping(GenerateTableOption tableOption) {
        return "/add";
    }

    public String getDetailRequestMapping(GenerateTableOption tableOption) {
        return "/detail";
    }

    public String getGetDetailRequestMapping(GenerateTableOption tableOption) {
        return "/get";
    }

    public String getEditRequestMapping(GenerateTableOption tableOption) {
        return "/edit";
    }

    public String getSaveRequestMapping(GenerateTableOption tableOption) {
        return "/save";
    }

    public String getUpdateRequestMapping(GenerateTableOption tableOption) {
        return "/update";
    }

    public String getDeleteRequestMapping(GenerateTableOption tableOption) {
        return "/delete";
    }

    @Override
    public BuilderType getBuilderType() {
        return BuilderType.CONTROLLER;
    }

    @Override
    public String getPackage(GenerateTableOption tableOption) {
        return "controller";
    }

    @Override
    public String getClassName(GenerateTableOption tableOption) {
        return tableOption.getModelName() + "Controller";
    }

}

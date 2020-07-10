package com.paladin.data.generate.build;

import com.paladin.common.core.FileResourceContainer;
import com.paladin.common.service.sys.vo.FileResource;
import com.paladin.data.generate.GenerateColumnOption;
import com.paladin.data.generate.GenerateTableOption;
import com.paladin.data.model.build.DbBuildColumn;
import com.paladin.framework.common.BaseModel;
import com.paladin.framework.service.IgnoreSelection;
import com.paladin.framework.utils.reflect.NameUtil;
import com.paladin.framework.utils.reflect.ReflectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ModelVOClassBuilder extends SpringBootClassBuilder {

    @Value("${paladin.generate.swagger:false}")
    private boolean startApi;

    protected boolean need(GenerateColumnOption columnOption) {
        DbBuildColumn buildColumn = columnOption.getBuildColumnOption();
        return columnOption.isPrimary() || (buildColumn != null && judge(buildColumn.getEditable()));
    }

    private boolean judge(Integer i) {
        return i != null && i == 1;
    }

    public String buildContent(GenerateTableOption tableOption) {

        Set<Class<?>> importClassSet = new HashSet<>();
        importClassSet.add(Getter.class);
        importClassSet.add(Setter.class);
        if(startApi) {
            importClassSet.add(ApiModel.class);
            importClassSet.add(ApiModelProperty.class);
        }
        List<GenerateColumnOption> originColumnOptions = tableOption.getColumnOptions();

        List<GenerateColumnOption> columnOptions = new ArrayList<>(originColumnOptions.size());
        for (GenerateColumnOption columnOption : originColumnOptions) {
            if (need(columnOption)) {
                columnOptions.add(columnOption);
            }
        }

        Collections.sort(columnOptions, (o1, o2) -> o1.getColumn().getOrderIndex() - o2.getColumn().getOrderIndex());

        boolean hasAttachment = false;

        for (GenerateColumnOption columnOption : columnOptions) {
            Class<?> clazz = columnOption.getFieldType();

            if (clazz.isArray())
                clazz = ReflectUtil.getArrayType(clazz);

            if (!clazz.isPrimitive() && !clazz.getName().matches("^java.lang.[^.]"))
                importClassSet.add(clazz);

            Integer isAtt = columnOption.getBuildColumnOption().getIsAttachment();
            if (isAtt != null && isAtt == BaseModel.BOOLEAN_YES) {
                importClassSet.add(FileResource.class);
                importClassSet.add(FileResourceContainer.class);
                importClassSet.add(IgnoreSelection.class);
                importClassSet.add(List.class);
                hasAttachment = true;
            }
        }

        String tab = "\t";

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(getClassPackage(tableOption)).append(";\n\n");

        String[] classNames = new String[importClassSet.size()];

        int i = 0;
        for (Class<?> importClass : importClassSet)
            classNames[i++] = importClass.getName();

        Arrays.sort(classNames);

        for (String className : classNames) {
            if (!className.matches("^java\\.lang\\.\\w+$"))
                sb.append("import ").append(className).append(";\n");
        }

        sb.append("\n@Getter ");
        sb.append("\n@Setter ");
        if(startApi) {
            sb.append("\n@ApiModel");
        }
        sb.append("\npublic class ").append(getClassName(tableOption));

        sb.append(" {\n\n");

        for (GenerateColumnOption columnOption : columnOptions) {
            if(startApi) {
                sb.append(tab).append("@ApiModelProperty(\"").append(columnOption.getColumn().getComment()).append("\")\n");
            } else {
                sb.append(tab).append("// ").append(columnOption.getColumn().getComment()).append("\n");
            }
            Integer isAtt = columnOption.getBuildColumnOption().getIsAttachment();
            if (isAtt != null && isAtt == BaseModel.BOOLEAN_YES) {
                sb.append(tab).append("@IgnoreSelection\n");
            }
            sb.append(tab).append("private ")
                    .append(columnOption.getFieldType().getSimpleName())
                    .append(" ")
                    .append(columnOption.getFieldName())
                    .append(";\n\n");
        }

        if (hasAttachment) {
            for (GenerateColumnOption columnOption : columnOptions) {
                Integer isAtt = columnOption.getBuildColumnOption().getIsAttachment();
                if (isAtt != null && isAtt == BaseModel.BOOLEAN_YES) {
                    sb.append(tab).append("public List<FileResource> get")
                            .append(NameUtil.firstUpperCase(columnOption.getFieldName())).append("File() {\n");
                    sb.append(tab).append(tab).append("if (").append(columnOption.getFieldName()).append(" != null && ")
                            .append(columnOption.getFieldName()).append(".length() > 0) {\n");
                    sb.append(tab).append(tab).append(tab).append("return FileResourceContainer.getFileResources(")
                            .append(columnOption.getFieldName()).append(".split(\",\"));\n");
                    sb.append(tab).append(tab).append("}\n");
                    sb.append(tab).append(tab).append("return null;\n");
                    sb.append(tab).append("}\n");
                }
            }
        }

        sb.append("}");

        return sb.toString();
    }

    @Override
    public BuilderType getBuilderType() {
        return BuilderType.MODEL_VO;
    }

    @Override
    public String getPackage(GenerateTableOption tableOption) {
        return "service.*.vo";
    }

    @Override
    public String getClassName(GenerateTableOption tableOption) {
        return tableOption.getModelName() + "VO";
    }

}

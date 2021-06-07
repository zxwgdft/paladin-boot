package com.paladin.data.generate.build;

import com.paladin.data.generate.GenerateColumnOption;
import com.paladin.data.generate.GenerateTableOption;
import com.paladin.data.model.build.DbBuildColumn;
import com.paladin.framework.constants.CommonConstants;
import com.paladin.framework.utils.reflect.NameUtil;
import com.paladin.framework.utils.reflect.ReflectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;


@Component
public class ModelDTOClassBuilder extends SpringBootClassBuilder {

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

        List<GenerateColumnOption> originColumnOptions = tableOption.getColumnOptions();
        List<GenerateColumnOption> columnOptions = new ArrayList<>(originColumnOptions.size());
        for (GenerateColumnOption columnOption : originColumnOptions) {
            if (need(columnOption)) {
                columnOptions.add(columnOption);
            }
        }
        Collections.sort(columnOptions, (o1, o2) -> o1.getColumn().getOrderIndex() - o2.getColumn().getOrderIndex());

        Set<Class<?>> importClassSet = new HashSet<>();
        importClassSet.add(Getter.class);
        importClassSet.add(Setter.class);
        if (startApi) {
            importClassSet.add(ApiModel.class);
            importClassSet.add(ApiModelProperty.class);
        }
        for (GenerateColumnOption columnOption : columnOptions) {
            Class<?> clazz = columnOption.getFieldType();

            if (clazz.isArray()) {
                clazz = ReflectUtil.getArrayType(clazz);
            }

            if (!clazz.isPrimitive() && !clazz.getName().matches("^java.lang.[^.]")) {
                importClassSet.add(clazz);
            }

            if (!columnOption.isPrimary()) {
                DbBuildColumn buildCol = columnOption.getBuildColumnOption();

                if (judge(buildCol.getRequired())) {
                    if (clazz == String.class) {
                        importClassSet.add(NotEmpty.class);
                    } else {
                        importClassSet.add(NotNull.class);
                    }
                }

                Integer maxLength = buildCol.getMaxLength();
                if (maxLength != null && maxLength > 0) {
                    importClassSet.add(Length.class);
                }
            }

            Integer isAtt = columnOption.getBuildColumnOption().getIsAttachment();
            if (isAtt != null && isAtt == CommonConstants.YES) {
                importClassSet.add(MultipartFile.class);
            }
        }

        String tab = "\t";

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(getClassPackage(tableOption)).append(";\n\n");

        String[] classNames = new String[importClassSet.size()];

        int i = 0;
        for (Class<?> importClass : importClassSet) {
            classNames[i++] = importClass.getName();
        }

        Arrays.sort(classNames);

        for (String className : classNames) {
            if (!className.matches("^java\\.lang\\.\\w+$"))
                sb.append("import ").append(className).append(";\n");
        }

        sb.append("\n@Getter ");
        sb.append("\n@Setter ");
        if (startApi) {
            sb.append("\n@ApiModel");
        }
        sb.append("\npublic class ").append(getClassName(tableOption));

        sb.append(" {\n\n");

        for (GenerateColumnOption columnOption : columnOptions) {
            String comment = columnOption.getColumn().getComment();
            if (startApi) {
                sb.append(tab).append("@ApiModelProperty(\"").append(comment).append("\")\n");
            } else {
                sb.append(tab).append("// ").append(comment).append("\n");
            }

            if (!columnOption.isPrimary()) {
                DbBuildColumn buildCol = columnOption.getBuildColumnOption();
                Class<?> clazz = columnOption.getFieldType();
                if (judge(buildCol.getRequired())) {
                    if (clazz == String.class) {
                        sb.append(tab).append("@NotEmpty(message = \"").append(comment).append("不能为空\")\n");
                    } else {
                        sb.append(tab).append("@NotNull(message = \"").append(comment).append("不能为空\")\n");
                    }
                }

                Integer maxLength = buildCol.getMaxLength();
                if (maxLength != null && maxLength > 0) {
                    sb.append(tab)
                            .append("@Length(max = ").append(maxLength)
                            .append(", message = \"").append(comment).append("长度不能大于").append(maxLength)
                            .append("\")\n");
                }
            }

            sb.append(tab).append("private ")
                    .append(columnOption.getFieldType().getSimpleName())
                    .append(" ")
                    .append(columnOption.getFieldName())
                    .append(";\n\n");

            Integer isAtt = columnOption.getBuildColumnOption().getIsAttachment();
            // 是否附件
            if (isAtt != null && isAtt == CommonConstants.YES) {
                sb.append(tab).append("private MultipartFile[] ")
                        .append(NameUtil.firstUpperCase(columnOption.getFieldName()))
                        .append("File")
                        .append(";\n\n");
            }

        }

        sb.append("}");

        return sb.toString();
    }

    @Override
    public BuilderType getBuilderType() {
        return BuilderType.MODEL_DTO;
    }

    @Override
    public String getPackage(GenerateTableOption tableOption) {
        return "service.*.dto";
    }

    @Override
    public String getClassName(GenerateTableOption tableOption) {
        return tableOption.getModelName() + "DTO";
    }


}

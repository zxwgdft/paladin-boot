package com.paladin.data.generate.build;

import com.paladin.data.generate.GenerateColumnOption;
import com.paladin.data.generate.GenerateEnvironment;
import com.paladin.data.generate.GenerateTableOption;
import com.paladin.framework.utils.reflect.NameUtil;
import com.paladin.framework.utils.reflect.ReflectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

@Component
public class ModelClassBuilder extends SpringBootClassBuilder {

    @Value("${paladin.generate.swagger:false}")
    private boolean startApi;

    private static Map<Class<?>, Set<String>> modelFieldMap = new HashMap<>();

    static {
        for (Class<?> type : GenerateEnvironment.baseModelTypeMap) {
            Set<String> fieldNames = new HashSet<>();
            Method[] methods = type.getMethods();
            for (Method method : methods) {
                if (ReflectUtil.isGetMethod(method)) {
                    String methodName = method.getName();
                    String name = NameUtil.removeGetOrSet(methodName);
                    fieldNames.add(name);
                }
            }

            if (fieldNames.size() > 0) {
                modelFieldMap.put(type, fieldNames);
            }
        }
    }

    /**
     * 获得基础实体类
     *
     * @param tableOption
     * @return
     */
    private Class<?> getBaseModelType(GenerateTableOption tableOption) {

        List<GenerateColumnOption> columnOptions = tableOption.getColumnOptions();

        Class<?> clazz = null;
        int fieldNum = 0;

        for (Entry<Class<?>, Set<String>> entry : modelFieldMap.entrySet()) {

            Set<String> fields = entry.getValue();
            int count = 0;
            for (GenerateColumnOption columnOption : columnOptions) {
                if (fields.contains(columnOption.getFieldName())) {
                    count++;
                }
            }

            if (count == fields.size()) {
                if (clazz == null || (clazz != null && fieldNum < count)) {
                    clazz = entry.getKey();
                    fieldNum = count;
                }
            }
        }

        return clazz;
    }

    private List<GenerateColumnOption> getNeedColumnOption(GenerateTableOption tableOption, Class<?> baseModelType) {
        List<GenerateColumnOption> columnOptions = tableOption.getColumnOptions();
        if (baseModelType != null) {
            ArrayList<GenerateColumnOption> result = new ArrayList<>(columnOptions.size());
            Set<String> baseModelFields = modelFieldMap.get(baseModelType);
            for (GenerateColumnOption columnOption : columnOptions) {
                if (!baseModelFields.contains(columnOption.getFieldName())) {
                    result.add(columnOption);
                }
            }
            return result;
        } else {
            return columnOptions;
        }
    }

    public String buildContent(GenerateTableOption tableOption) {

        Set<Class<?>> importClassSet = new HashSet<>();
        Class<?> baseModelType = getBaseModelType(tableOption);
        List<GenerateColumnOption> columnOptions = getNeedColumnOption(tableOption, baseModelType);

        Collections.sort(columnOptions, (o1, o2) -> o1.getColumn().getOrderIndex() - o2.getColumn().getOrderIndex());

        importClassSet.add(Getter.class);
        importClassSet.add(Setter.class);
        importClassSet.add(Id.class);
        if(startApi) {
            importClassSet.add(ApiModel.class);
            importClassSet.add(ApiModelProperty.class);
        }
        if (baseModelType != null) {
            importClassSet.add(baseModelType);
        }

        for (GenerateColumnOption columnOption : columnOptions) {
            Class<?> clazz = columnOption.getFieldType();

            if (clazz.isArray())
                clazz = ReflectUtil.getArrayType(clazz);

            if (!clazz.isPrimitive() && !clazz.getName().matches("^java.lang.[^.]"))
                importClassSet.add(clazz);
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
        if(startApi) {
            sb.append("\n@ApiModel");
        }
        sb.append("\npublic class ").append(tableOption.getModelName());
        if (baseModelType != null) {
            sb.append(" extends ").append(baseModelType.getSimpleName());
        }

        sb.append(" {\n\n");

        for (GenerateColumnOption columnOption : columnOptions) {

            if(startApi) {
                sb.append(tab).append("@ApiModelProperty(\"").append(columnOption.getColumn().getComment()).append("\")\n");
            } else {
                sb.append(tab).append("// ").append(columnOption.getColumn().getComment()).append("\n");
            }

            if (columnOption.isPrimary()) {
                sb.append(tab).append("@Id").append("\n");
            }

            sb.append(tab).append("private ").append(columnOption.getFieldType().getSimpleName()).append(" ").append(columnOption.getFieldName())
                    .append(";\n\n");
        }

        sb.append("}");

        return sb.toString();
    }

    @Override
    public BuilderType getBuilderType() {
        return BuilderType.MODEL;
    }

    @Override
    public String getPackage(GenerateTableOption tableOption) {
        return "model";
    }

    @Override
    public String getClassName(GenerateTableOption tableOption) {
        return tableOption.getModelName();
    }


}

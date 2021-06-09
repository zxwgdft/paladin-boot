package com.paladin.data.generate.build;

import com.paladin.data.generate.GenerateTableOption;
import com.paladin.framework.service.PageParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class QueryDTOBuilder extends SpringBootClassBuilder {

    public String buildContent(GenerateTableOption tableOption) {

        Set<Class<?>> importClassSet = new HashSet<>();

        importClassSet.add(PageParam.class);
        importClassSet.add(Getter.class);
        importClassSet.add(Setter.class);

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
        sb.append("\npublic class ").append(getClassName(tableOption)).append(" extends ").append(PageParam.class.getSimpleName()).append(" {\n\n");
        sb.append("}");

        return sb.toString();
    }

    @Override
    public BuilderType getBuilderType() {
        return BuilderType.QUERY_DTO;
    }

    @Override
    public String getPackage(GenerateTableOption tableOption) {
        return "service.*.dto";
    }

    @Override
    public String getClassName(GenerateTableOption tableOption) {
        return tableOption.getModelName() + "Query";
    }

}

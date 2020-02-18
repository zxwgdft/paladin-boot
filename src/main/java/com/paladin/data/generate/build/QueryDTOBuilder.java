package com.paladin.data.generate.build;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.paladin.data.generate.GenerateTableOption;
import com.paladin.framework.common.OffsetPage;

@Component
public class QueryDTOBuilder extends SpringBootClassBuilder {

	public String buildContent(GenerateTableOption tableOption) {

		Set<Class<?>> importClassSet = new HashSet<>();

		importClassSet.add(OffsetPage.class);

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

		sb.append("\npublic class ").append(getClassName(tableOption)).append(" extends ").append(OffsetPage.class.getSimpleName()).append(" {\n\n");
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

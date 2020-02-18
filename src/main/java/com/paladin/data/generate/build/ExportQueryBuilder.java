package com.paladin.data.generate.build;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.paladin.common.core.export.ExportCondition;
import com.paladin.data.generate.GenerateBuilderContainer;
import com.paladin.data.generate.GenerateTableOption;

@Component
public class ExportQueryBuilder extends SpringBootClassBuilder {
	
	public String buildContent(GenerateTableOption tableOption) {

		Set<Class<?>> importClassSet = new HashSet<>();

		importClassSet.add(ExportCondition.class);
		
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

		sb.append("import ").append(GenerateBuilderContainer.getClassImportPackage(BuilderType.QUERY_DTO, tableOption)).append(";\n");
		
		sb.append("\npublic class ").append(getClassName(tableOption)).append(" extends ").append(ExportCondition.class.getSimpleName()).append(" {\n\n");		
		
		String queryClass = GenerateBuilderContainer.getClassName(BuilderType.QUERY_DTO, tableOption);
		sb.append("\tprivate ").append(queryClass).append(" query;\n\n");
		sb.append("\tpublic ").append(queryClass).append(" getQuery() {\n");
		sb.append("\t\treturn query;\n");
		sb.append("\t}\n\n");
		sb.append("\tpublic void setQuery(").append(queryClass).append(" query) {\n");
		sb.append("\t\tthis.query = query;\n");
		sb.append("\t}\n\n");
		sb.append("}");

		return sb.toString();
	}

	@Override
	public BuilderType getBuilderType() {
		return BuilderType.EXPORT_QUERY_DTO;
	}

	@Override
	public String getPackage(GenerateTableOption tableOption) {
		return "controller.*.dto";
	}

	@Override
	public String getClassName(GenerateTableOption tableOption) {
		return tableOption.getModelName() + "ExportCondition";
	}

}

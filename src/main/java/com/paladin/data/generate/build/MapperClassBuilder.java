package com.paladin.data.generate.build;

import org.springframework.stereotype.Component;

import com.paladin.data.generate.GenerateBuilderContainer;
import com.paladin.data.generate.GenerateTableOption;
import com.paladin.framework.core.configuration.mybatis.CustomMapper;

@Component
public class MapperClassBuilder extends SpringBootClassBuilder {

	public String buildContent(GenerateTableOption tableOption) {

		StringBuilder sb = new StringBuilder();

		sb.append("package ").append(getClassPackage(tableOption)).append(";\n\n");
		sb.append("import ").append(GenerateBuilderContainer.getClassImportPackage(BuilderType.MODEL, tableOption)).append(";\n");
		sb.append("import ").append(CustomMapper.class.getName()).append(";\n\n");

		sb.append("public interface ").append(getClassName(tableOption)).append(" extends ").append(CustomMapper.class.getSimpleName()).append("<")
				.append(tableOption.getModelName()).append(">{\n\n}");

		return sb.toString();
	}

	@Override
	public BuilderType getBuilderType() {
		return BuilderType.MAPPER;
	}

	@Override
	public String getPackage(GenerateTableOption tableOption) {
		return "mapper";
	}

	@Override
	public String getClassName(GenerateTableOption tableOption) {
		return tableOption.getModelName() + "Mapper";
	}
}

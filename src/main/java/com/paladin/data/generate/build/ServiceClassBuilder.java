package com.paladin.data.generate.build;

import com.paladin.data.generate.GenerateBuilderContainer;
import com.paladin.data.generate.GenerateTableOption;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.paladin.framework.core.ServiceSupport;

@Component
public class ServiceClassBuilder extends SpringBootClassBuilder {

	public String buildContent(GenerateTableOption tableOption) {

		StringBuilder sb = new StringBuilder();

		sb.append("package ").append(getClassPackage(tableOption)).append(";\n\n");

		sb.append("import ").append(Service.class.getName()).append(";\n\n");

		sb.append("import ").append(GenerateBuilderContainer.getClassImportPackage(BuilderType.MODEL, tableOption)).append(";\n");
		sb.append("import ").append(ServiceSupport.class.getName()).append(";\n\n");

		sb.append("@Service\n");
		sb.append("public class ").append(tableOption.getModelName()).append("Service extends ").append(ServiceSupport.class.getSimpleName()).append("<")
				.append(tableOption.getModelName()).append("> {\n");
		sb.append("\n");
		sb.append("}");

		return sb.toString();
	}

	@Override
	public BuilderType getBuilderType() {
		return BuilderType.SERVICE;
	}

	@Override
	public String getPackage(GenerateTableOption tableOption) {
		return "service";
	}

	@Override
	public String getClassName(GenerateTableOption tableOption) {
		return tableOption.getModelName() + "Service";
	}
}

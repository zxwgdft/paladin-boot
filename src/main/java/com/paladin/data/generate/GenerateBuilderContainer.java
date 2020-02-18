package com.paladin.data.generate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.paladin.data.generate.build.BuilderType;
import com.paladin.data.generate.build.FileBuilder;
import com.paladin.data.generate.build.SpringBootClassBuilder;
import com.paladin.data.generate.build.SpringBootPageBuilder;
import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.spring.SpringContainer;

@Component
public class GenerateBuilderContainer implements SpringContainer {

	static Map<BuilderType, FileBuilder> builderMap = new HashMap<>();

	@Override
	public boolean initialize() {
		Map<String, FileBuilder> builders = SpringBeanHelper.getBeansByType(FileBuilder.class);

		for (FileBuilder builder : builders.values()) {
			builderMap.put(builder.getBuilderType(), builder);
		}

		return true;
	}

	@Override
	public boolean afterInitialize() {
		return true;
	}

	@Override
	public int order() {
		return 0;
	}

	public static FileBuilder getFileContentBuilder(BuilderType type) {
		return builderMap.get(type);
	}

	public static String getClassImportPackage(BuilderType type, GenerateTableOption tableOption) {
		SpringBootClassBuilder classbuilder = (SpringBootClassBuilder) builderMap.get(type);
		if (classbuilder != null) {
			return classbuilder.getImportPackage(tableOption);
		}
		return null;
	}

	public static String getClassName(BuilderType type, GenerateTableOption tableOption) {
		SpringBootClassBuilder classbuilder = (SpringBootClassBuilder) builderMap.get(type);
		if (classbuilder != null) {
			return classbuilder.getClassName(tableOption);
		}
		return null;
	}

	public static String getViewPath(BuilderType type, GenerateTableOption tableOption) {
		SpringBootPageBuilder classbuilder = (SpringBootPageBuilder) builderMap.get(type);
		if (classbuilder != null) {
			return classbuilder.getViewPath(tableOption);
		}
		return null;
	}
}

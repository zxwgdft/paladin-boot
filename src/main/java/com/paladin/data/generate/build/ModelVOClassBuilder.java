package com.paladin.data.generate.build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.paladin.data.generate.GenerateColumnOption;
import com.paladin.data.generate.GenerateTableOption;
import com.paladin.data.model.build.DbBuildColumn;
import com.paladin.framework.utils.reflect.NameUtil;
import com.paladin.framework.utils.reflect.ReflectUtil;

@Component
public class ModelVOClassBuilder extends SpringBootClassBuilder {

	public String buildContent(GenerateTableOption tableOption) {

		Set<Class<?>> importClassSet = new HashSet<>();

		List<GenerateColumnOption> originColumnOptions = tableOption.getColumnOptions();

		List<GenerateColumnOption> columnOptions = new ArrayList<>(originColumnOptions.size());
		for (GenerateColumnOption columnOption : originColumnOptions) {
			if (need(columnOption)) {
				columnOptions.add(columnOption);
			}
		}

		Collections.sort(columnOptions, new Comparator<GenerateColumnOption>() {
			@Override
			public int compare(GenerateColumnOption o1, GenerateColumnOption o2) {
				return o1.getColumn().getOrderIndex() - o2.getColumn().getOrderIndex();
			}
		});

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
		for (Class<?> importClass : importClassSet)
			classNames[i++] = importClass.getName();

		Arrays.sort(classNames);

		for (String className : classNames) {
			if (!className.matches("^java\\.lang\\.\\w+$"))
				sb.append("import ").append(className).append(";\n");
		}

		sb.append("\npublic class ").append(getClassName(tableOption));

		sb.append(" {\n\n");

		for (GenerateColumnOption columnOption : columnOptions) {

			sb.append(tab).append("// ").append(columnOption.getColumn().getComment()).append("\n");

			if (columnOption.isPrimary()) {
			}

			sb.append(tab).append("private ").append(columnOption.getFieldType().getSimpleName()).append(" ").append(columnOption.getFieldName())
					.append(";\n\n");
		}

		for (GenerateColumnOption columnOption : columnOptions) {

			String fieldName = columnOption.getFieldName();
			String className = columnOption.getFieldType().getSimpleName();

			// getMethod
			sb.append(tab).append("public ").append(className).append(" ").append(NameUtil.addGet(fieldName)).append("() {\n").append(tab).append(tab)
					.append("return ").append(fieldName).append(";\n").append(tab).append("}\n\n");

			// setMethod
			sb.append(tab).append("public void ").append(NameUtil.addSet(fieldName)).append("(").append(className).append(" ").append(fieldName).append(") {\n")
					.append(tab).append(tab).append("this.").append(fieldName).append(" = ").append(fieldName).append(";\n").append(tab).append("}\n\n");
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

	protected boolean need(GenerateColumnOption columnOption) {
		DbBuildColumn buildColumn = columnOption.getBuildColumnOption();
		return columnOption.isPrimary() || buildColumn != null;
	}

}

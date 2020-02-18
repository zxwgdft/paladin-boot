package com.paladin.data.generate.build;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Id;

import org.springframework.stereotype.Component;

import java.util.Set;

import com.paladin.data.generate.GenerateColumnOption;
import com.paladin.data.generate.GenerateEnvironment;
import com.paladin.data.generate.GenerateTableOption;
import com.paladin.data.model.build.DbBuildColumn;
import com.paladin.framework.utils.reflect.NameUtil;
import com.paladin.framework.utils.reflect.ReflectUtil;

import tk.mybatis.mapper.annotation.IgnoreInMultipleResult;

@Component
public class ModelClassBuilder extends SpringBootClassBuilder {

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

		Collections.sort(columnOptions, new Comparator<GenerateColumnOption>() {

			@Override
			public int compare(GenerateColumnOption o1, GenerateColumnOption o2) {
				return o1.getColumn().getOrderIndex() - o2.getColumn().getOrderIndex();
			}

		});

		importClassSet.add(Id.class);
		if (baseModelType != null) {
			importClassSet.add(baseModelType);
		}

		for (GenerateColumnOption columnOption : columnOptions) {
			Class<?> clazz = columnOption.getFieldType();

			if (clazz.isArray())
				clazz = ReflectUtil.getArrayType(clazz);

			if (!clazz.isPrimitive() && !clazz.getName().matches("^java.lang.[^.]"))
				importClassSet.add(clazz);

			if (judgeLargeText(columnOption.getBuildColumnOption())) {
				importClassSet.add(IgnoreInMultipleResult.class);
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

		sb.append("\npublic class ").append(tableOption.getModelName());
		if (baseModelType != null) {
			sb.append(" extends ").append(baseModelType.getSimpleName());
		}

		sb.append(" {\n\n");

		for (GenerateColumnOption columnOption : columnOptions) {

			sb.append(tab).append("// ").append(columnOption.getColumn().getComment()).append("\n");

			if (columnOption.isPrimary()) {
				sb.append(tab).append("@Id").append("\n");
			}

			if (judgeLargeText(columnOption.getBuildColumnOption())) {
				sb.append(tab).append("@IgnoreInMultipleResult").append("\n");
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

	private boolean judgeLargeText(DbBuildColumn dbBuildColumn) {
		if (dbBuildColumn == null)
			return false;
		Integer i = dbBuildColumn.getLargeText();
		return i != null && i == 1;
	}

}

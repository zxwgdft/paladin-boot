package com.paladin.data.generate.build;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import com.paladin.data.generate.GenerateBuilderContainer;
import com.paladin.data.generate.GenerateColumnOption;
import com.paladin.data.generate.GenerateTableOption;
import com.paladin.data.model.build.DbBuildColumn;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class PageAddBuilder extends SpringBootPageBuilder {

	private static Template template = FreemarkerUtil.getTemplate("/template_page_add.temp");

	public String buildContent(GenerateTableOption tableOption) {
		HashMap<String, Object> data = new HashMap<>();

		HashSet<String> enumcodes = new HashSet<>();
		StringBuilder sb = new StringBuilder();
		List<GenerateColumnOption> columnOptions = tableOption.getColumnOptions();

		Collections.sort(columnOptions, new Comparator<GenerateColumnOption>() {
			public int compare(GenerateColumnOption o1, GenerateColumnOption o2) {
				Integer oi1 = o1.getColumn().getOrderIndex();
				Integer oi2 = o2.getColumn().getOrderIndex();

				if (oi1 == null)
					return -1;
				if (oi2 == null)
					return 1;
				return oi1 - oi2;
			}
		});

		sb.append("[\n");

		for (GenerateColumnOption columnOption : columnOptions) {
			if (columnOption.isPrimary()) {
				continue;
			}

			DbBuildColumn buildOption = columnOption.getBuildColumnOption();
			if (buildOption == null) {
				continue;
			}

			if (!judge(buildOption.getAddable())) {
				continue;
			}

			sb.append("\t\t\t\t{ title: \"").append(buildOption.getTitle()).append("\"");
			sb.append(", name: \"").append(columnOption.getFieldName()).append("\"");

			String inputType = "TEXT";

			if (judge(buildOption.getEnumCode())) {
				sb.append(", enum: \"").append(buildOption.getEnumCode()).append("\"");
				enumcodes.add(buildOption.getEnumCode());

				if (judge(buildOption.getMultiSelect())) {
					inputType = "CHECKBOX";
				} else {
					inputType = "SELECT";
				}
			}

			if (judge(buildOption.getIsAttachment())) {
				sb.append(", fileName: \"").append(columnOption.getFieldName()).append("File").append("\"");
				Integer count = buildOption.getAttachmentCount();
				if (count != 0 && count > 0) {
					sb.append(", maxFileCount: ").append(count).append("");
				}
				inputType = "ATTACHMENT";
			}

			if (judge(buildOption.getRequired())) {
				sb.append(", required: \"required\"");
			}

			Integer maxLength = buildOption.getMaxLength();
			if (maxLength != null && maxLength > 0) {
				sb.append(", maxLength: ").append(maxLength).append("");
			}

			if (Date.class.isAssignableFrom(columnOption.getFieldType())) {
				inputType = "DATE";
			}

			sb.append(", inputType: \"").append(inputType).append("\"");
			sb.append(" },\n");
		}

		sb.deleteCharAt(sb.length() - 2);
		sb.append("\t\t\t\t]");

		if (enumcodes.size() > 0) {
			String enumStr = "<tt:constant enumcode=\"";
			for (String enumcode : enumcodes) {
				enumStr += enumcode + ",";
			}
			enumStr += "\"/>";
			data.put("enumcodes", enumStr);
		} else {
			data.put("enumcodes", "");
		}

		data.put("columns", sb.toString());
		data.put("title", "新增");
		data.put("mainTitle", tableOption.getTitle());
		data.put("mainModel", tableOption.getModel());

		ControllerClassBuilder controllerBuilder = (ControllerClassBuilder) GenerateBuilderContainer.getFileContentBuilder(BuilderType.CONTROLLER);
		data.put("saveUrl", controllerBuilder.getBaseRequestMapping(tableOption) + controllerBuilder.getSaveRequestMapping(tableOption));
		data.put("indexUrl", controllerBuilder.getBaseRequestMapping(tableOption) + controllerBuilder.getIndexRequestMapping(tableOption));

		StringWriter writer = new StringWriter();
		try {
			template.process(data, writer);
		} catch (TemplateException | IOException e) {
			throw new RuntimeException("自动生成page_detail失败", e);
		}
		return writer.toString();
	}

	@Override
	public BuilderType getBuilderType() {
		return BuilderType.PAGE_ADD;
	}

	@Override
	public String getFileName(GenerateTableOption tableOption) {
		return tableOption.getTable().getName() + "_add.html";
	}

	private boolean judge(Integer i) {
		return i != null && i == 1;
	}

	private boolean judge(String s) {
		return s != null && s.length() > 0;
	}
}

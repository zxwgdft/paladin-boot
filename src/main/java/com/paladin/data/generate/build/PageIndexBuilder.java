package com.paladin.data.generate.build;

import java.io.IOException;
import java.io.StringWriter;
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
public class PageIndexBuilder extends SpringBootPageBuilder {

	private static Template template = FreemarkerUtil.getTemplate("/template_page_index.temp");

	public String buildContent(GenerateTableOption tableOption) {
		HashMap<String, Object> data = new HashMap<>();

		HashSet<String> enumcodes = new HashSet<>();
		StringBuilder sb = new StringBuilder();
		List<GenerateColumnOption> columnOptions = tableOption.getColumnOptions();
		for (GenerateColumnOption columnOption : columnOptions) {
			DbBuildColumn buildOption = columnOption.getBuildColumnOption();

			if (columnOption.isPrimary() || buildOption == null || judge(buildOption.getLargeText()) || !judge(buildOption.getTableable())) {
				continue;
			}

			sb.append("\t\t\t\t\t{ title: \"").append(buildOption.getTitle()).append("\", field: \"").append(columnOption.getFieldName()).append("\"");

			if (judge(buildOption.getEnumCode())) {
				sb.append(", enumcode: \"").append(buildOption.getEnumCode()).append("\"");
				enumcodes.add(buildOption.getEnumCode());
			}

			if (Date.class.isAssignableFrom(columnOption.getFieldType())) {
				sb.append(" ,formatter: \"date\"");
			}
			sb.append(" },\n");
		}

		sb.deleteCharAt(sb.length() - 1);

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

		data.put("tableColumns", sb.toString());
		data.put("mainTitle", tableOption.getTitle());

		ControllerClassBuilder controllerBuilder = (ControllerClassBuilder) GenerateBuilderContainer.getFileContentBuilder(BuilderType.CONTROLLER);
		data.put("searchUrl", controllerBuilder.getBaseRequestMapping(tableOption) + controllerBuilder.getFindPageRequestMapping(tableOption));
		data.put("exportUrl", controllerBuilder.getBaseRequestMapping(tableOption) + controllerBuilder.getExportRequestMapping(tableOption));
		data.put("addUrl", controllerBuilder.getBaseRequestMapping(tableOption) + controllerBuilder.getAddRequestMapping(tableOption));
		data.put("detailUrl", controllerBuilder.getBaseRequestMapping(tableOption) + controllerBuilder.getDetailRequestMapping(tableOption));
		data.put("removeUrl", controllerBuilder.getBaseRequestMapping(tableOption) + controllerBuilder.getDeleteRequestMapping(tableOption));
		data.put("mainModel", tableOption.getModel());

		StringWriter writer = new StringWriter();
		try {
			template.process(data, writer);
		} catch (TemplateException | IOException e) {
			throw new RuntimeException("自动生成page_index失败", e);
		}
		return writer.toString();
	}

	@Override
	public BuilderType getBuilderType() {
		return BuilderType.PAGE_INDEX;
	}

	@Override
	public String getFileName(GenerateTableOption tableOption) {
		return tableOption.getTable().getName() + "_index.html";
	}

	private boolean judge(Integer i) {
		return i != null && i == 1;
	}

	private boolean judge(String s) {
		return s != null && s.length() > 0;
	}
}

package com.paladin.data.generate.build;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.paladin.data.generate.GenerateTableOption;
import com.paladin.framework.core.exception.BusinessException;

public abstract class SpringBootClassBuilder implements FileBuilder {

	@Override
	public void buildFile(GenerateTableOption tableOption, String projectPath) {
		try {
			Path path = getBootProjectClassPath(tableOption, projectPath);
			String content = this.buildContent(tableOption);
			Files.write(path, content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException("创建文件[" + getBuilderType() + "]失败");
		}
	}

	/**
	 * 获取spring boot项目类的路径
	 * 
	 * @param projectPath
	 * @param classPackage
	 * @param className
	 * @return
	 * @throws IOException
	 */
	public Path getBootProjectClassPath(GenerateTableOption tableOption, String projectPath) throws IOException {

		String classPackage = getClassPackage(tableOption);
		String fileName = getFileName(tableOption);

		if (projectPath != null && projectPath.length() > 0) {
			projectPath = projectPath.replaceAll("\\\\", "/");
			projectPath += projectPath.endsWith("/") ? "src/main/java" : "/src/main/java";
		}

		String[] more = null;
		if (classPackage == null || classPackage.length() == 0) {
			more = new String[] { fileName };
		} else {
			more = classPackage.split("\\.");
			Files.createDirectories(Paths.get(projectPath, more));
			more = Arrays.copyOf(more, more.length + 1);
			more[more.length - 1] = fileName;
		}

		Path path = Paths.get(projectPath, more);
		return path;
	}

	public String getClassPackage(GenerateTableOption tableOption) {

		String basePackage = tableOption.getBasePackage();
		String model = tableOption.getModel();
		String subModel = tableOption.getSubModel();

		if (model != null && model.length() != 0) {
			basePackage += "." + model;
		}

		String packagee = "";

		if (subModel != null && subModel.length() != 0) {
			String[] subModels = subModel.split("\\.");
			for (String sm : subModels) {
				packagee += "." + sm;
			}
			packagee = packagee.substring(1);
			String packgeeTemp = getPackage(tableOption);
			if (packgeeTemp != null && packgeeTemp.length() != 0) {
				if (packgeeTemp.indexOf("*") >= 0) {
					packagee = basePackage + "." + packgeeTemp.replaceFirst("\\*", packagee);
				} else {
					packagee = basePackage + "." + packgeeTemp + "." + packagee;
				}
			} else {
				packagee = basePackage + "." + packagee;
			}
		} else {
			String packgeeTemp = getPackage(tableOption);
			if (packgeeTemp != null && packgeeTemp.length() != 0) {
				packagee = basePackage + "." + packgeeTemp.replaceFirst("\\.\\*\\.", "");
			} else {
				packagee = basePackage;
			}
		}

		return packagee;
	}

	public String getImportPackage(GenerateTableOption tableOption) {
		return getClassPackage(tableOption) + "." + getClassName(tableOption);
	}

	public abstract String getPackage(GenerateTableOption tableOption);

	public abstract String getClassName(GenerateTableOption tableOption);

	public String getFileName(GenerateTableOption tableOption) {
		return getClassName(tableOption) + ".java";
	}
}

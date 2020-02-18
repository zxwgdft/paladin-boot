package com.paladin.data.generate.build;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.paladin.data.generate.GenerateTableOption;
import com.paladin.framework.core.exception.BusinessException;

public abstract class SpringBootResourceBuilder implements FileBuilder {

	@Override
	public void buildFile(GenerateTableOption tableOption, String projectPath) {
		try {
			Path path = getBootProjectResourcesPath(projectPath, tableOption);
			String content = this.buildContent(tableOption);
			Files.write(path, content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException("创建文件[" + getBuilderType() + "]失败");
		}
	}

	/**
	 * 获取spring boot资源地址
	 * 
	 * @param projectPath
	 * @param tableOption
	 * @param generateType
	 * @return
	 * @throws IOException
	 */
	public Path getBootProjectResourcesPath(String projectPath, GenerateTableOption tableOption) throws IOException {

		if (projectPath != null && projectPath.length() > 0) {
			projectPath = projectPath.replaceAll("\\\\", "/");
			projectPath += projectPath.endsWith("/") ? "src/main/resources" : "/src/main/resources";
		} else {
			throw new IOException("项目路径不能为空");
		}

		String[] subPathArray = getRelationPath(tableOption);

		String[] dirPath = Arrays.copyOf(subPathArray, subPathArray.length - 1);
		Files.createDirectories(Paths.get(projectPath, dirPath));
		return Paths.get(projectPath, subPathArray);
	}

	public String[] getRelationPath(GenerateTableOption tableOption) {

		String model = tableOption.getModel();
		String subModel = tableOption.getSubModel();

		ArrayList<String> subPaths = new ArrayList<>();

		// 基本路径
		addMutilPath(getResourceBasePath(tableOption), subPaths);
		// 主模块，不能有多层（带.）
		if (model != null && model.length() != 0) {
			subPaths.add(model);
		}
		// 子模块，可以多层
		addMutilPath(subModel, subPaths);
		// 最终路径
		addMutilPath(getResourceLastPath(tableOption), subPaths);
		// 文件名
		subPaths.add(getFileName(tableOption));

		String[] subPathArray = new String[subPaths.size()];
		subPathArray = subPaths.toArray(subPathArray);
		return subPathArray;
	}

	private void addMutilPath(String pathStr, List<String> paths) {
		if (pathStr != null && pathStr.length() != 0) {
			String[] bps = pathStr.split("\\.");
			for (String bp : bps) {
				paths.add(bp);
			}
		}
	}

	/**
	 * 资源所在基本路径，例如web页面或templates
	 * 
	 * @return
	 */
	public abstract String getResourceBasePath(GenerateTableOption tableOption);

	/**
	 * 资源所在最终路径，在分配主次模块后最后还想要分的模块路径，一般为null
	 * 
	 * @param tableOption
	 * @return
	 */
	public String getResourceLastPath(GenerateTableOption tableOption) {
		return null;
	}

	public abstract String getFileName(GenerateTableOption tableOption);

}

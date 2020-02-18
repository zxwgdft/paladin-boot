package com.paladin.data.generate.build;

import com.paladin.data.generate.GenerateTableOption;

public abstract class SpringBootPageBuilder extends SpringBootResourceBuilder {

	@Override
	public String getResourceBasePath(GenerateTableOption tableOption) {
		return "templates";
	}

	public String getViewPath(GenerateTableOption tableOption) {

		String path = "/";
		String model = tableOption.getModel();
		String subModel = tableOption.getSubModel();

		// 主模块，不能有多层（带.）
		if (model != null && model.length() != 0) {
			path += model;
		}

		// 子模块，可以多层
		path = addPaths(subModel, path);
		// 最终路径
		path = addPaths(getResourceLastPath(tableOption), path);

		// 文件名
		String filename = getFileName(tableOption);
		filename = filename.substring(0, filename.lastIndexOf("."));
		path += "/" + filename;

		return path;
	}

	private String addPaths(String pathStr, String path) {
		if (pathStr != null && pathStr.length() > 0) {
			String[] bps = pathStr.split("\\.");
			for (String bp : bps) {
				path += "/" + bp;
			}
		}
		return path;
	}
}

package com.paladin.data.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paladin.data.controller.dto.ColumnDTO;
import com.paladin.data.database.model.Column;
import com.paladin.data.generate.GenerateBuilderContainer;
import com.paladin.data.generate.GenerateColumnOption;
import com.paladin.data.generate.GenerateTableOption;
import com.paladin.data.generate.build.BuilderType;
import com.paladin.data.generate.build.FileBuilder;
import com.paladin.data.model.build.DbBuildColumn;
import com.paladin.data.model.build.DbBuildTable;
import com.paladin.data.service.build.DbBuildColumnService;
import com.paladin.data.service.build.DbBuildTableService;
import com.paladin.data.service.vo.BuildColumnVO;
import com.paladin.framework.core.copy.SimpleBeanCopier.SimpleBeanCopyUtil;

@Service
public class GenerateService {

	@Autowired
	private DbBuildColumnService buildColumnService;

	@Autowired
	private DbBuildTableService buildTableService;

	@Autowired
	private DBConnectionService connectionService;

	public List<BuildColumnVO> getBuildColumn(String dbName, String tableName) {
		Column[] columns = connectionService.getDBTableColumns(dbName, tableName);
		List<ColumnDTO> columnDTOs = SimpleBeanCopyUtil.simpleCopyList(Arrays.asList(columns), ColumnDTO.class);
		List<DbBuildColumn> buildColumns = buildColumnService.getDbBuildColumn(dbName, tableName);

		HashMap<String, DbBuildColumn> map = null;
		if (buildColumns != null && buildColumns.size() > 0) {
			map = new HashMap<>();
			for (DbBuildColumn buildColumn : buildColumns) {
				map.put(buildColumn.getColumnName(), buildColumn);
			}
		}

		List<BuildColumnVO> result = new ArrayList<>(columnDTOs.size());
		for (ColumnDTO column : columnDTOs) {
			BuildColumnVO buildColumn = new BuildColumnVO();
			buildColumn.setColumn(column);
			if (map != null) {
				DbBuildColumn dbc = map.get(column.getName());
				if (dbc != null) {
					SimpleBeanCopyUtil.simpleCopy(dbc, buildColumn);
				}
			}
			result.add(buildColumn);
		}

		return result;
	}

	/**
	 * 创建文件内容
	 * 
	 * @param tableOption
	 * @param generateType
	 * @return 如果有文件内容构建器则调用构建，无则返回空字符串
	 */
	public String buildFileContent(GenerateTableOption tableOption, BuilderType generateType) {
		FileBuilder builder = GenerateBuilderContainer.getFileContentBuilder(generateType);
		if (builder != null) {
			return builder.buildContent(tableOption);
		}

		return "";
	}

	/**
	 * 创建spring boot项目文件
	 * 
	 * @param tableOption
	 * @param generateType
	 * @param content
	 */
	public void buildProjectFile(GenerateTableOption tableOption, BuilderType generateType, String projectPath) {
		FileBuilder builder = GenerateBuilderContainer.getFileContentBuilder(generateType);
		if (builder != null) {
			builder.buildFile(tableOption, projectPath);
		}

	}

	/**
	 * 保存构建方案
	 * 
	 * @param tableOption
	 * @param dbName
	 */
	@Transactional
	public void saveBuildOption(GenerateTableOption tableOption, String dbName) {
		String tableName = tableOption.getTable().getName();

		buildTableService.removeByTable(dbName, tableName);
		buildColumnService.removeByTable(dbName, tableName);

		DbBuildTable buildTable = new DbBuildTable();
		buildTable.setConnectionName(dbName);
		buildTable.setTableName(tableName);
		buildTable.setTableTitle(tableOption.getTitle());

		buildTableService.save(buildTable);

		for (GenerateColumnOption columnOption : tableOption.getColumnOptions()) {
			DbBuildColumn dbc = columnOption.getBuildColumnOption();
			if (dbc != null) {

				dbc.setId(null);
				dbc.setConnectionName(dbName);
				dbc.setTableName(tableName);

				buildColumnService.save(dbc);
			}
		}
	}

}

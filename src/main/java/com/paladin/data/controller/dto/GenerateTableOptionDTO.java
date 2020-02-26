package com.paladin.data.controller.dto;

import java.util.List;

import com.paladin.data.model.build.DbBuildColumn;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @see com.paladin.data.generate.GenerateTableOption
 * @since 2018年4月11日
 */
@Getter
@Setter
public class GenerateTableOptionDTO {

    private String dbName;

    private String tableName;

    private String title;

    private String basePackage;

    private String model;

    private String subModel;

    private String projectPath;

    private String filePath;

    private List<DbBuildColumn> columnBuildOptions;

}

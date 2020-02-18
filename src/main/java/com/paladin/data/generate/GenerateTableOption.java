package com.paladin.data.generate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paladin.data.database.DataBaseType;
import com.paladin.data.database.model.Column;
import com.paladin.data.database.model.Table;
import com.paladin.data.database.model.constraint.ColumnConstraint;
import com.paladin.data.database.model.constraint.TableConstraint;
import com.paladin.framework.utils.reflect.NameUtil;

/**
 * 自动创建配置类
 * 
 * @author TontoZhou
 * @since 2018年4月11日
 */
public class GenerateTableOption {

	/**
	 * 具体表描述
	 */
	private Table table;

	/**
	 * 主标题
	 */
	private String title;

	/**
	 * 基础路径，配合{@code model}属性
	 * <p>
	 * 例如数据库管理的业务包com.paladin.data，则basePackage = com.paladin
	 * </p>
	 */
	private String basePackage;

	/**
	 * 模块
	 * <p>
	 * 例如
	 * data.database.connection，则会在data模块下相应的model，mapper，service下建立database.connection包，并在其下创建相应
	 * </p>
	 */
	private String model;

	/**
	 * 子模块
	 * <p>
	 * 例如connection，则会在data模块下相应的model，mapper，service下建立connection包，并在其下创建相应类
	 * </p>
	 */
	private String subModel;

	/**
	 * 实体类名称
	 */
	private String modelName;

	/**
	 * 数据库类型
	 */
	private DataBaseType dataBaseType;

	/**
	 * 表列对应配置
	 */
	private Map<String, GenerateColumnOption> columnOptions;

	public GenerateTableOption(Table table, DataBaseType dataBaseType) {
		this.table = table;
		this.dataBaseType = dataBaseType;

		this.modelName = NameUtil.firstUpperCase(NameUtil.underline2hump(table.getName()));

		Column[] columns = table.getChildren();

		columnOptions = new HashMap<>();

		for (Column column : columns) {
			columnOptions.put(column.getName(), new GenerateColumnOption(column, dataBaseType));
		}
	}

	public GenerateColumnOption getColumnOption(String columnName) {
		return columnOptions.get(columnName);
	}

	public List<GenerateColumnOption> getColumnOptions() {
		return new ArrayList<>(columnOptions.values());
	}

	public String[] getPrimaryName() {
		TableConstraint tableConstraint = table.getPrimaryConstraint();
		if (tableConstraint != null) {
			ColumnConstraint[] constraints = table.getPrimaryConstraint().getConstraint();
			if (constraints != null) {
				String[] names = new String[constraints.length];
				for (int i = 0; i < names.length; i++) {
					names[i] = NameUtil.underline2hump(constraints[i].getColumn());
				}
				return names;
			}
		}
		return null;
	}

	public String getFirstPrimaryName() {
		String[] primaries = getPrimaryName();
		return primaries != null ? primaries[0] : null;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public DataBaseType getDataBaseType() {
		return dataBaseType;
	}

	public Table getTable() {
		return table;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getSubModel() {
		return subModel;
	}

	public void setSubModel(String subModel) {
		this.subModel = subModel;
	}

	public String getTitle() {
		return title == null || title.length() == 0 ? table.getName() : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

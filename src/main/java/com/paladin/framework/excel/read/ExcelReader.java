package com.paladin.framework.excel.read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paladin.framework.excel.ICell;
import com.paladin.framework.excel.IRow;
import com.paladin.framework.excel.ISheet;
import com.paladin.framework.excel.PropertyValidate;
import com.paladin.framework.utils.reflect.InstanceCreator;

public class ExcelReader<T> {
	/*
	 * 根据类型找到对应的{@link ReadPropertyConvert}和{@link PropertyValidate}方法
	 */
	private Map<Class<?>, ReadPropertyConvert<?>> classConvertCacheMap = new HashMap<>();
	private Map<Class<?>, PropertyValidate> classValidateCacheMap = new HashMap<>();;

	/*
	 * 根据名称找到对应的{@link ReadPropertyConvert}和{@link PropertyValidate}方法
	 */
	private Map<String, ReadPropertyConvert<?>> nameConvertCacheMap = new HashMap<>();;
	private Map<String, PropertyValidate> nameValidateCacheMap = new HashMap<>();;

	/**
	 * 增加指定类型转换器
	 * 
	 * @param convert
	 * @param clazz
	 */
	public void addPropertyConvert(ReadPropertyConvert<?> convert, Class<?> clazz) {
		classConvertCacheMap.put(clazz, convert);
	}

	/**
	 * 增加指定属性的转换器（可以路径寻找子类）
	 * 
	 * @param convert
	 * @param name
	 */
	public void addPropertyConvert(ReadPropertyConvert<?> convert, String name) {
		nameConvertCacheMap.put(name, convert);
	}

	/**
	 * 增加指定类型的验证器
	 * 
	 * @param validate
	 * @param clazz
	 */
	public void addPropertyConvert(PropertyValidate validate, Class<?> clazz) {
		classValidateCacheMap.put(clazz, validate);
	}

	/**
	 * 增加指定属性的验证器（可以路径寻找子类）
	 * 
	 * @param validate
	 * @param name
	 */
	public void addPropertyConvert(PropertyValidate validate, String name) {
		nameValidateCacheMap.put(name, validate);
	}

	protected List<ReadColumn> columns;

	// 工作簿
	protected ISheet sheet;
	// 当前所在行号
	protected int currentRowIndex;
	// 最大行号
	protected int lastRowIndex;
	// 是否在读取一行记录的数据错误的情况下继续下一行
	protected boolean continueIfDataError = false;

	// 实例创建器
	protected InstanceCreator<T> instanceCreator;

	public ExcelReader(Class<T> target, List<ReadColumn> columns, ISheet sheet) {
		this(target, columns, sheet, 0);
	}

	public ExcelReader(Class<T> target, List<ReadColumn> columns, ISheet sheet, int startRow) {
		this(new InstanceCreator<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T createInstance() {
				if (target == Map.class || target == HashMap.class) {
					return (T) new HashMap<String, Object>();
				} else {
					try {
						return target.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {

					}
				}
				return null;
			}
		}, columns, sheet, startRow);
	}

	public ExcelReader(InstanceCreator<T> instanceCreator, List<ReadColumn> columns, ISheet sheet, int startRow) {
		this.instanceCreator = instanceCreator;
		this.columns = columns;
		this.currentRowIndex = startRow;
		this.sheet = sheet;
		this.lastRowIndex = sheet.getLastRowNum();
	}

	/**
	 * 创建实例
	 * 
	 * @return
	 * @throws ExcelReadException
	 */

	/**
	 * 顺序读取一行
	 * 
	 * @return
	 * @throws ExcelReadException
	 */
	public T readRow() throws ExcelReadException {
		return readRow(instanceCreator.createInstance());
	}

	/***
	 * 顺序读取一行
	 * 
	 * @return
	 * @throws ExcelReadException
	 *             解析数据时未指明的其他错误
	 */
	public T readRow(T obj) throws ExcelReadException {
		if (!hasNext()) {
			return null;
		}

		int rowindex = currentRowIndex++;

		IRow row = sheet.getRow(rowindex);

		if (row == null) {
			return null;
		}

		int size = columns.size();
		int nullcount = 0;

		List<A> cache = new ArrayList<>();
		
		for (int i = 0; i < size; i++) {

			ReadColumn column = columns.get(i);

			int cellIndex = column.getCellIndex();
			ICell cell = row.getCell(cellIndex);

			ReadPropertyConvert<?> convert = column.getConvert();
			if (convert == null) {
				convert = nameConvertCacheMap.get(column.getId());
			}
			if (convert == null) {
				convert = classConvertCacheMap.get(column.getType());
			}

			Object value = null;

			try {
				if (convert == null) {
					value = column.convertValue(cell);
				} else {
					value = convert.convert(cell);
				}
				
				if(value == null) {
					nullcount++;
				}

				PropertyValidate validate = column.getValidate();
				if (validate == null) {
					validate = nameValidateCacheMap.get(column.getId());
				}
				if (validate == null) {
					validate = classValidateCacheMap.get(column.getType());
				}

				if (validate == null) {
					String validMessage = column.validateValue(value);
					if (validMessage != null)
						throw new CellValueException(rowindex, column, validMessage);
				} else {
					// 特殊验证需要在填完所有值后进行
					cache.add(new A(column, value, validate));
				}

				if (value != null) {
					column.fillValue(obj, value);
				}
			} catch (CellValueException e) {
				throw e;
			} catch (ExcelReadException e) {
				throw new CellValueException(rowindex, column, e.getMessage());
			}
		}

		// 值全为NULL，则判断该行没有数据
		if(nullcount == size) {
			return null;
		}
		
		for (A a : cache) {
			if (!a.validate.validate(obj, a.value)) {
				throw new CellValueException(rowindex, a.column, a.validate.getMessage());
			}
		}

		return obj;
	}

	/**
	 * 顺序读取所有行数
	 * 
	 * @return
	 * @throws ExcelReadException
	 */
	public List<T> readRows() throws ExcelReadException {
		return readRows(lastRowIndex - currentRowIndex + 1);
	}

	/**
	 * 顺序读取行数据
	 * 
	 * @param size
	 *            读取个数
	 * @return
	 * @throws ExcelReadException
	 */
	public List<T> readRows(int size) throws ExcelReadException {
		List<T> rows = readRows(currentRowIndex, size);
		currentRowIndex += rows.size();
		return rows;
	}

	/**
	 * 读取行数据
	 * 
	 * @param start
	 *            读取起始位置
	 * @param size
	 *            读取个数
	 * @return
	 * @throws ExcelReadException
	 */
	public List<T> readRows(int start, int size) throws ExcelReadException {
		if (start >= 0 && size > 0 && start <= lastRowIndex) {
			size = Math.min(size, lastRowIndex - start + 1);
			List<T> resultList = new ArrayList<T>(size);
			for (; size > 0; size--) {
				T obj = null;

				try {
					obj = readRow();
				} catch (CellValueException e) {
					if (!continueIfDataError) {
						throw e;
					} else {
						continue;
					}
				}

				if (obj != null) {
					resultList.add(obj);
				}
			}

			return resultList;
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * 是否还有行数据
	 * 
	 * @return
	 */
	public boolean hasNext() {
		return currentRowIndex <= lastRowIndex;
	}

	/**
	 * 是否在读取一行记录的数据错误的情况下继续下一行
	 */
	public boolean isContinueIfDataError() {
		return continueIfDataError;
	}

	/**
	 * 是否在读取一行记录的数据错误的情况下继续下一行
	 */
	public void setContinueIfDataError(boolean continueIfDataError) {
		this.continueIfDataError = continueIfDataError;
	}

	public int getLastRowIndex() {
		return lastRowIndex;
	}
	
	class A {

		private A(ReadColumn column, Object value, PropertyValidate validate) {
			this.column = column;
			this.value = value;
			this.validate = validate;
		}

		ReadColumn column;
		PropertyValidate validate;
		Object value;

	}



}

package com.paladin.data.database.constant;

import java.util.HashMap;
import java.util.Map;

import com.paladin.data.database.DataBaseType;

public class PropertyTypeConstant {

	private final static Map<DataBaseType, Map<String, PropertyType>> propertyTypeMap;
	
	static {
		propertyTypeMap = new HashMap<>();

		Map<String, PropertyType> oracleTypeMap = new HashMap<>();
		oracleTypeMap.put(OracleTypeConstant.TYPE_BFILE, PropertyType.BYTES);
		oracleTypeMap.put(OracleTypeConstant.TYPE_BINARY_DOUBLE, PropertyType.NUMBER);
		oracleTypeMap.put(OracleTypeConstant.TYPE_BINARY_FLOAT, PropertyType.NUMBER);
		oracleTypeMap.put(OracleTypeConstant.TYPE_BLOB, PropertyType.BYTES);
		oracleTypeMap.put(OracleTypeConstant.TYPE_CLOB, PropertyType.STRING);
		oracleTypeMap.put(OracleTypeConstant.TYPE_CHAR, PropertyType.STRING);
		oracleTypeMap.put(OracleTypeConstant.TYPE_DATE, PropertyType.TIME);
		oracleTypeMap.put(OracleTypeConstant.TYPE_FLOAT, PropertyType.NUMBER);
		oracleTypeMap.put(OracleTypeConstant.TYPE_INTEGER, PropertyType.NUMBER);
		oracleTypeMap.put(OracleTypeConstant.TYPE_LONG, PropertyType.STRING);
		oracleTypeMap.put(OracleTypeConstant.TYPE_LONGRAW, PropertyType.BYTES);
		oracleTypeMap.put(OracleTypeConstant.TYPE_NCHAR, PropertyType.STRING);
		oracleTypeMap.put(OracleTypeConstant.TYPE_NCLOB, PropertyType.STRING);
		oracleTypeMap.put(OracleTypeConstant.TYPE_NUMBER, PropertyType.NUMBER);
		oracleTypeMap.put(OracleTypeConstant.TYPE_NVARCHAR2, PropertyType.STRING);
		oracleTypeMap.put(OracleTypeConstant.TYPE_RAW, PropertyType.BYTES);
		oracleTypeMap.put(OracleTypeConstant.TYPE_TIMESTAMP, PropertyType.TIME);
		oracleTypeMap.put(OracleTypeConstant.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE, PropertyType.TIME);
		oracleTypeMap.put(OracleTypeConstant.TYPE_TIMESTAMP_WITH_TIME_ZONE, PropertyType.TIME);
		oracleTypeMap.put(OracleTypeConstant.TYPE_VARCHAR, PropertyType.STRING);
		oracleTypeMap.put(OracleTypeConstant.TYPE_VARCHAR2, PropertyType.STRING);
		propertyTypeMap.put(DataBaseType.ORACLE, oracleTypeMap);
		
		

		Map<String, PropertyType> mysqlTypeMap = new HashMap<>();
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_INT, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_BIGINT, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_BIT, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_BLOB, PropertyType.BYTES);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_BOOLEAN, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_CHAR, PropertyType.STRING);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_DATE, PropertyType.TIME);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_DATETIME, PropertyType.TIME);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_DECIMAL, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_DOUBLE, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_FLOAT, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_INTEGER, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_MEDIUMINT, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_SMALLINT, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_TEXT, PropertyType.STRING);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_TIME, PropertyType.TIME);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_TIMESTAMP, PropertyType.TIME);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_TINYINT, PropertyType.NUMBER);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_VARCHAR, PropertyType.STRING);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_YEAR, PropertyType.TIME);	
		propertyTypeMap.put(DataBaseType.MYSQL, mysqlTypeMap);

	}
	
	public static PropertyType getPropertyType(DataBaseType dbType,String dataType){
		Map<String,PropertyType> typeMap = propertyTypeMap.get(dbType);
		if(typeMap != null)
			return typeMap.get(dataType);
		throw new IllegalArgumentException("不支持数据库["+dbType+"]");
	}
	
	public static Map<String, PropertyType> getPropertyTypes(DataBaseType dbType){
		Map<String,PropertyType> typeMap = propertyTypeMap.get(dbType);
		if(typeMap == null)
			throw new IllegalArgumentException("不支持数据库["+dbType+"]");
		return typeMap;
	}
}

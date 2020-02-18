package com.paladin.data.database;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.paladin.data.database.constant.MySqlTypeConstant;
import com.paladin.data.database.constant.OracleTypeConstant;
import com.paladin.data.database.model.Column;

public class DataTypeUtil {
	
private final static Map<DataBaseType, Map<String, Class<?>>> propertyTypeMap;
	
	static {
		propertyTypeMap = new HashMap<>();

		Map<String, Class<?>> oracleTypeMap = new HashMap<>();
		oracleTypeMap.put(OracleTypeConstant.TYPE_BFILE, byte[].class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_BINARY_DOUBLE, Double.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_BINARY_FLOAT, Float.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_BLOB, byte[].class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_CLOB, String.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_CHAR, String.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_DATE, Date.class);
		// Float 特殊处理
		//oracleTypeMap.put(OracleTypeConstant.TYPE_FLOAT, PropertyType.NUMBER);
		oracleTypeMap.put(OracleTypeConstant.TYPE_INTEGER, Integer.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_LONG, String.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_LONGRAW, byte[].class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_NCHAR, String.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_NCLOB, String.class);
		// Number 特殊处理
		//oracleTypeMap.put(OracleTypeConstant.TYPE_NUMBER, PropertyType.NUMBER);
		oracleTypeMap.put(OracleTypeConstant.TYPE_NVARCHAR2, String.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_RAW, byte[].class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_TIMESTAMP, Date.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE, Date.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_TIMESTAMP_WITH_TIME_ZONE, Date.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_VARCHAR, String.class);
		oracleTypeMap.put(OracleTypeConstant.TYPE_VARCHAR2, String.class);
		
		propertyTypeMap.put(DataBaseType.ORACLE, oracleTypeMap);
		
		

		Map<String, Class<?>> mysqlTypeMap = new HashMap<>();
		// 需要判断有无符号
		//mysqlTypeMap.put(MySqlTypeConstant.TYPE_INT, PropertyType.NUMBER);
		//mysqlTypeMap.put(MySqlTypeConstant.TYPE_BIGINT, PropertyType.NUMBER);
		//mysqlTypeMap.put(MySqlTypeConstant.TYPE_INTEGER, PropertyType.NUMBER);				
		
		// 需要判断精度
		//mysqlTypeMap.put(MySqlTypeConstant.TYPE_BIT, PropertyType.NUMBER);
		
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_MEDIUMINT, Integer.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_BLOB, byte[].class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_BOOLEAN, Boolean.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_CHAR, String.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_DATE, Date.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_DATETIME, Date.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_DECIMAL, BigDecimal.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_DOUBLE, Double.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_FLOAT, Float.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_SMALLINT, Integer.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_TEXT, String.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_TIME,  Date.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_TIMESTAMP,  Date.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_TINYINT, Integer.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_VARCHAR,  String.class);
		mysqlTypeMap.put(MySqlTypeConstant.TYPE_YEAR,  Date.class);
		propertyTypeMap.put(DataBaseType.MYSQL, mysqlTypeMap);

	}
	
		
	/**
	 * 获取数据类型对应java类型，缺省情况返回String
	 * @param column
	 * @param dbType
	 * @return
	 */
	public static Class<?> getJavaType(Column column,DataBaseType  dbType)
	{
		if(column == null || dbType == null)
			 throw new IllegalArgumentException("表列和数据库类型不能	");
		
		String dataType = column.getDataType();
		
		 Map<String, Class<?>> classMap  = propertyTypeMap.get(dbType);
		 if(classMap == null)
			 throw new IllegalArgumentException("不支持数据库["+dbType+"]");
		 
		 Class<?> clazz = classMap.get(dataType);
		 if(clazz == null)
		 {
			 if(dbType == DataBaseType.ORACLE )
			 {
				 if(OracleTypeConstant.TYPE_NUMBER.equals(dataType))
					 return getOracleNumberClass(column);
				 if(OracleTypeConstant.TYPE_FLOAT.equals(dataType))
					 return getOracleFloatClass(column);
			 }
			 else  if(dbType == DataBaseType.MYSQL )
			 {
				 if(MySqlTypeConstant.TYPE_INT.equals(dataType) || MySqlTypeConstant.TYPE_INTEGER.equals(dataType))
				 {
					 return column.isUnsigned()? Long.class:Integer.class;
				 }
				 
				 if(MySqlTypeConstant.TYPE_BIGINT.equals(dataType))
				 {
					 return column.isUnsigned()? BigInteger.class:Long.class;
				 }
				 
				 if(MySqlTypeConstant.TYPE_BIT.equals(dataType))
				 {
					 return column.getDataPrecision()>1? byte[].class:Boolean.class;
				 }
				 
			 }
		 }
		 else
			 return clazz;
		 
		 return String.class;
	}
	
	
	
	/**
	 * 
	 * precision				class
	 * 	<20					Float
	 * >=20					Double
	 * 
	 * 
	 * @param column
	 * @return
	 */
	private static Class<?> getOracleFloatClass(Column column) {
		Integer precision = column.getDataPrecision();
		return (precision != null && precision < 20) ? Float.class : Double.class;
	}
	
	/**
	 * 
	 * Oracle Number 特殊处理
	 * 
	 * 
	 * precision		scale		class
	 * 	<=10			<=0		Integer
	 * <=19;>10	<=0		Long
	 * >19						BigDecimal
	 * 
	 * ------------------ 小数不准确 ----------------------
	 * 					<7;>0	Float					
	 * 					<16;>0	Double				
	 * 					>=16		BigDecimal
	 * 
	 * @param column
	 * @param type
	 * @return
	 */
	private static Class<?> getOracleNumberClass(Column column) {

		Integer scale = column.getDataScale();
		Integer precision = column.getDataPrecision();

		if (scale == null || precision == null)
			return BigDecimal.class;

		if (scale <= 0) {
			if (precision <= 10)
				return Integer.class;
			if (precision <= 19)
				return Long.class;

			return BigDecimal.class;
		} else {
			if (scale < 7)
				return Float.class;
			if (scale < 16)
				return Double.class;

			return BigDecimal.class;
		}
	}
}

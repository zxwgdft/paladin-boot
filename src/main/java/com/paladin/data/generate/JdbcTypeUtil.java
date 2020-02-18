package com.paladin.data.generate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;

import com.paladin.data.database.DataBaseType;
import com.paladin.data.database.constant.MySqlTypeConstant;
import com.paladin.data.database.constant.OracleTypeConstant;


public class JdbcTypeUtil {
	
	
	private static final Map<DataBaseType,Map<String,JdbcType>> jdbcMap;
	private static final Map<Class<?>, JdbcType> java2JdbcMap;
	
	static{	
		
		jdbcMap = new HashMap<>();
		java2JdbcMap = new HashMap<>();
		
		Map<String,JdbcType> mysqlMap = new HashMap<>();
		Map<String,JdbcType> oracleMap = new HashMap<>();
		
		// 以下类型对应不一定正确，但是常规类型可以保证（待确认）
		
		mysqlMap.put(MySqlTypeConstant.TYPE_BIGINT, JdbcType.BIGINT);
		mysqlMap.put(MySqlTypeConstant.TYPE_BIT, JdbcType.BIT);
		mysqlMap.put(MySqlTypeConstant.TYPE_BLOB, JdbcType.BLOB);
		mysqlMap.put(MySqlTypeConstant.TYPE_BOOLEAN, JdbcType.BOOLEAN);
		mysqlMap.put(MySqlTypeConstant.TYPE_CHAR,JdbcType.CHAR);
		mysqlMap.put(MySqlTypeConstant.TYPE_DATE,JdbcType.DATE);
		mysqlMap.put(MySqlTypeConstant.TYPE_DATETIME,JdbcType.DATE);
		mysqlMap.put(MySqlTypeConstant.TYPE_DECIMAL,JdbcType.DECIMAL);
		mysqlMap.put(MySqlTypeConstant.TYPE_DOUBLE,JdbcType.DOUBLE);	
		mysqlMap.put(MySqlTypeConstant.TYPE_FLOAT,JdbcType.FLOAT);
		mysqlMap.put(MySqlTypeConstant.TYPE_INT,JdbcType.INTEGER);
		mysqlMap.put(MySqlTypeConstant.TYPE_INTEGER,JdbcType.INTEGER);
		mysqlMap.put(MySqlTypeConstant.TYPE_MEDIUMINT,JdbcType.INTEGER);
		mysqlMap.put(MySqlTypeConstant.TYPE_SMALLINT,JdbcType.SMALLINT);
		mysqlMap.put(MySqlTypeConstant.TYPE_TEXT,JdbcType.LONGVARCHAR);
		mysqlMap.put(MySqlTypeConstant.TYPE_TIME,JdbcType.TIME);
		mysqlMap.put(MySqlTypeConstant.TYPE_TIMESTAMP,JdbcType.TIMESTAMP);
		mysqlMap.put(MySqlTypeConstant.TYPE_TINYINT,JdbcType.TINYINT);
		mysqlMap.put(MySqlTypeConstant.TYPE_VARCHAR,JdbcType.VARCHAR);
		mysqlMap.put(MySqlTypeConstant.TYPE_YEAR,JdbcType.INTEGER);
		
		
		oracleMap.put(OracleTypeConstant.TYPE_BFILE, JdbcType.BLOB);
		oracleMap.put(OracleTypeConstant.TYPE_BINARY_DOUBLE, JdbcType.DOUBLE);
		oracleMap.put(OracleTypeConstant.TYPE_BINARY_FLOAT, JdbcType.FLOAT);
		oracleMap.put(OracleTypeConstant.TYPE_BLOB, JdbcType.BLOB);
		oracleMap.put(OracleTypeConstant.TYPE_CHAR, JdbcType.CHAR);	
		oracleMap.put(OracleTypeConstant.TYPE_CLOB, JdbcType.CLOB);
		oracleMap.put(OracleTypeConstant.TYPE_DATE, JdbcType.DATE);
		oracleMap.put(OracleTypeConstant.TYPE_FLOAT, JdbcType.FLOAT);	
		oracleMap.put(OracleTypeConstant.TYPE_INTEGER, JdbcType.INTEGER);
		oracleMap.put(OracleTypeConstant.TYPE_LONG, JdbcType.BIGINT);
		oracleMap.put(OracleTypeConstant.TYPE_LONGRAW, JdbcType.BLOB);
		oracleMap.put(OracleTypeConstant.TYPE_NCHAR, JdbcType.VARCHAR);
		oracleMap.put(OracleTypeConstant.TYPE_NCLOB, JdbcType.CLOB);
		oracleMap.put(OracleTypeConstant.TYPE_NUMBER, JdbcType.NUMERIC);
		oracleMap.put(OracleTypeConstant.TYPE_NVARCHAR2, JdbcType.VARCHAR);
		oracleMap.put(OracleTypeConstant.TYPE_RAW, JdbcType.BLOB);		
		oracleMap.put(OracleTypeConstant.TYPE_TIMESTAMP, JdbcType.TIMESTAMP);
		oracleMap.put(OracleTypeConstant.TYPE_TIMESTAMP_WITH_LOCAL_TIME_ZONE, JdbcType.TIMESTAMP);
		oracleMap.put(OracleTypeConstant.TYPE_TIMESTAMP_WITH_TIME_ZONE, JdbcType.TIMESTAMP);
		oracleMap.put(OracleTypeConstant.TYPE_VARCHAR, JdbcType.VARCHAR);
		oracleMap.put(OracleTypeConstant.TYPE_VARCHAR2, JdbcType.VARCHAR);
	
		
		jdbcMap.put(DataBaseType.MYSQL, mysqlMap);
		jdbcMap.put(DataBaseType.ORACLE, oracleMap);
		
		java2JdbcMap.put(String.class, JdbcType.VARCHAR);
		
		java2JdbcMap.put(Boolean.class, JdbcType.BOOLEAN);
		java2JdbcMap.put(boolean.class, JdbcType.BOOLEAN);
		
		java2JdbcMap.put(Byte.class, JdbcType.INTEGER);
		java2JdbcMap.put(byte.class, JdbcType.INTEGER);	
		
		java2JdbcMap.put(Short.class, JdbcType.SMALLINT);
		java2JdbcMap.put(short.class, JdbcType.SMALLINT);
		
		java2JdbcMap.put(Integer.class, JdbcType.INTEGER);
		java2JdbcMap.put(int.class, JdbcType.INTEGER);		
		
		java2JdbcMap.put(Long.class, JdbcType.BIGINT);
		java2JdbcMap.put(long.class, JdbcType.BIGINT);
		
		java2JdbcMap.put(Float.class, JdbcType.FLOAT);
		java2JdbcMap.put(float.class, JdbcType.FLOAT);
		
		java2JdbcMap.put(Double.class, JdbcType.DOUBLE);
		java2JdbcMap.put(double.class, JdbcType.DOUBLE);
		
		java2JdbcMap.put(BigInteger.class, JdbcType.BIGINT);
		java2JdbcMap.put(BigDecimal.class, JdbcType.NUMERIC);
		
		java2JdbcMap.put(Byte[].class, JdbcType.BLOB);	
		java2JdbcMap.put(byte[].class, JdbcType.BLOB);	
		
		java2JdbcMap.put(Date.class, JdbcType.TIMESTAMP);

	}
	
	public static JdbcType getJdbcType(DataBaseType dbType,String dataType){
		return jdbcMap.get(dbType).get(dataType);
	}

	public static JdbcType getJdbcType(Class<?> javaType){
		return java2JdbcMap.get(javaType);
	}
	
}

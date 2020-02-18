package com.paladin.framework.core.configuration.mybatis;

import java.util.Properties;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.paladin.framework.core.GlobalProperties;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * MyBatis扫描接口，使用的tk.mybatis.spring.mapper.MapperScannerConfigurer，如果你不使用通用Mapper
 */
@Configuration
@AutoConfigureAfter(MyBatisConfiguration.class)
public class MyBatisMapperScannerConfig {
	
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(Environment env) {
		String basePackage = env.getProperty("paladin.mybatis.mapper-package");
		if(basePackage == null || basePackage.length() == 0) {
			basePackage = "com.paladin.common.mapper,com.paladin.data.mapper,com.paladin." + GlobalProperties.project + ".mapper";
		}
		
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		mapperScannerConfigurer.setBasePackage(basePackage);

		Properties mapperProperties = new Properties();
		mapperProperties.setProperty("mappers", CustomMapper.class.getName() + "," + CustomJoinMapper.class.getName());
		mapperProperties.setProperty("notEmpty", "false");
		mapperProperties.setProperty("IDENTITY", "MYSQL");
		mapperScannerConfigurer.setProperties(mapperProperties);

		return mapperScannerConfigurer;
	}

}

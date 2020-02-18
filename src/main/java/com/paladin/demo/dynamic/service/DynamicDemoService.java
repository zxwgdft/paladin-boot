package com.paladin.demo.dynamic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.paladin.data.dynamic.SqlSessionContainer;
import com.paladin.demo.dynamic.mapper.DynamicDemoMapper;
import com.paladin.demo.dynamic.model.DynamicDemo;

@Service
@ConditionalOnProperty(prefix = "paladin", value = "dynamic-datasource-enabled", havingValue = "true", matchIfMissing = false)
public class DynamicDemoService {

	@Autowired
	private SqlSessionContainer sqlSessionContainer;

	public void testDemo() {
		// 使用mysql数据源
		sqlSessionContainer.setCurrentDataSource("mysql");
		printDemo(sqlSessionContainer.getSqlSessionTemplate().getMapper(DynamicDemoMapper.class).getDemoFromMysql());

		// 切换sqlserver数据源
		sqlSessionContainer.setCurrentDataSource("sqlserver");
		printDemo(sqlSessionContainer.getSqlSessionTemplate().getMapper(DynamicDemoMapper.class).getDemoFromSqlServer());
	}

	private void printDemo(DynamicDemo demo) {
		System.out.println("数据源：" + demo.getName() + "/ 时间：" + demo.getTime());
	}

}

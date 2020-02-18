package com.paladin.data.dynamic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInterceptor;
import com.paladin.framework.spring.SpringContainer;

@Component
@ConditionalOnProperty(prefix = "paladin", value = "dynamic-datasource-enabled", havingValue = "true", matchIfMissing = false)
public class SqlSessionContainer implements SpringContainer {

	@Resource
	private SqlSessionFactoryProperties properties;

	private SqlSessionFactory sqlSessionFactory;
	private SqlSessionTemplate sqlSessionTemplate;

	private DynamicDataSource dataSource;

	public boolean initialize() {
		try {
			sqlSessionFactory = buildSqlSessionFactory(properties);
			sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 设置当前数据源
	 * 
	 * @param name
	 */
	public void setCurrentDataSource(String name) {
		dataSource.setCurrentDataSource(name);
	}

	/**
	 * 获取SqlSession，需要手动关闭
	 * 
	 * @return
	 */
	public SqlSession getSqlSession() {
		return sqlSessionFactory.openSession();
	}
	
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	private SqlSessionFactory buildSqlSessionFactory(SqlSessionFactoryProperties properties) throws IOException {
		SimpleSqlSessionFactoryBuilder builder = new SimpleSqlSessionFactoryBuilder();

		dataSource = new DynamicDataSource();

		builder.setDataSource(dataSource);
		builder.setTypeAliasesPackage(properties.getTypeAliasesPackage());

		List<Interceptor> plugins = new ArrayList<>();

		// 分页插件
		if (properties.isPageEnabled()) {
			PageInterceptor pageHelper = new PageInterceptor();
			Properties pageProperties = new Properties();
			pageProperties.setProperty("reasonable", "true");
			pageProperties.setProperty("supportMethodsArguments", "true");
			pageProperties.setProperty("returnPageInfo", "check");
			pageProperties.setProperty("params", "count=countSql");
			
			// 动态数据源分页方言
			pageProperties.setProperty("autoRuntimeDialect", "true");			
			pageHelper.setProperties(pageProperties);

			plugins.add(pageHelper);
		}

		/*
		 * 添加插件，如果需要可以查看QueryInterceptor等插件
		 */
		builder.setPlugins(plugins.toArray(new Interceptor[plugins.size()]));

		// 添加XML目录
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		builder.setMapperLocations(resolver.getResources(properties.getMapperLocation()));
		return builder.build();
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}



}

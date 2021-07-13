package com.paladin.data.dynamic;

import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "paladin", value = "dynamic-datasource-enabled", havingValue = "true", matchIfMissing = false)
public class SqlSessionContainer implements ApplicationRunner {

    @Resource
    private SqlSessionFactoryProperties properties;

    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionTemplate sqlSessionTemplate;

    private DynamicDataSource dataSource;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            sqlSessionFactory = buildSqlSessionFactory(properties);
            sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
            log.info("初始化动态数据源会话容器");
        } catch (IOException e) {
            log.error("初始化动态数据源会话容器异常", e);
        }
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

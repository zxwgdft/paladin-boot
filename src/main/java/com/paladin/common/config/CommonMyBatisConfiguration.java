package com.paladin.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author TontoZhou
 * @since 2020/1/3
 */
@Component
public class CommonMyBatisConfiguration implements InitializingBean {

    @Autowired
    public SqlSessionFactory sqlSessionFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 不使用CODE ENUM形式的常量处理
        // 修改mybatis默认枚举处理器
        // sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().setDefaultEnumTypeHandler(EnumTypeHandler.class);
    }


}

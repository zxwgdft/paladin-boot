package com.paladin.framework.core.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.paladin.framework.core.ServiceSupport;
import com.paladin.framework.core.configuration.mybatis.CustomMapper;
import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.spring.SpringContainer;
import com.paladin.framework.utils.reflect.ReflectUtil;

/**
 * 业务支持类容器，启动时为{@link ServiceSupport}自动注入SqlMapper
 *
 * @author TontoZhou
 */
@Component
public class ServiceSupportConatiner implements SpringContainer {

    private static Logger logger = LoggerFactory.getLogger(ServiceSupportConatiner.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public boolean initialize() {

        /**
         * 根据泛型类型为service support注入相应的sqlMapper
         */

        Map<String, CustomMapper> customMappers = SpringBeanHelper.getBeansByType(CustomMapper.class);
        Map<String, ServiceSupport> serviceSupports = SpringBeanHelper.getBeansByType(ServiceSupport.class);

        Map<Class<?>, CustomMapper> mapperMap = new HashMap<>();

        for (Entry<String, CustomMapper> entry : customMappers.entrySet()) {
            CustomMapper mapper = entry.getValue();
            Class<?> genericType = ReflectUtil.getSuperClassArgument(mapper.getClass(), CustomMapper.class, 0);

            if (genericType == null || genericType == Object.class) {
                logger.warn("[" + mapper.getClass().getName() + "]的实现类没有明确定义[" + CustomMapper.class.getName() + "]的泛型");
                continue;
            }

            CustomMapper oldMapper = mapperMap.get(genericType);
            if (oldMapper != null)
                logger.warn("实体类[" + genericType.getName() + "]存在多个CustomMapper实现类，[" + oldMapper.getClass().getName() + "]将被覆盖");

            mapperMap.put(genericType, mapper);
        }


        for (Entry<String, ServiceSupport> entry : serviceSupports.entrySet()) {
            ServiceSupport support = entry.getValue();
            Class<?> genericType = ReflectUtil.getSuperClassArgument(support.getClass(), ServiceSupport.class, 0);

            if (genericType == null || genericType == Object.class) {
                logger.warn("[" + support.getClass().getName() + "]的实现类没有明确定义[" + ServiceSupport.class.getName() + "]的泛型，无法为其注册CustomMapper");
                continue;
            }

            CustomMapper mapper = mapperMap.get(genericType);
            if (mapper == null) {
                logger.warn("实体类[" + genericType.getName() + "]没有对应的[" + CustomMapper.class.getName() + "]的实现类");
                continue;
            } else {
                support.setSqlMapper(mapper);
                logger.info("===>为[" + support.getClass().getName() + "]注入CustomMapper<===");
            }

            support.init();
            logger.info("===>[" + support.getClass().getName() + "]初始化成功<===");
        }

        return true;
    }

    @Override
    public boolean afterInitialize() {
        return true;
    }

    @Override
    public int order() {
        return -1;
    }

}

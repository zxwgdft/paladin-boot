package com.paladin.framework.service;

import com.paladin.framework.mybatis.CustomMapper;
import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.spring.SpringContainer;
import com.paladin.framework.utils.reflect.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 业务支持类容器，启动时为{@link ServiceSupport}自动注入SqlMapper
 *
 * @author TontoZhou
 */
@Component
@Slf4j
public class ServiceSupportConatiner implements SpringContainer {

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
                log.warn("[" + mapper.getClass().getName() + "]的实现类没有明确定义[" + CustomMapper.class.getName() + "]的泛型");
                continue;
            }

            CustomMapper oldMapper = mapperMap.get(genericType);
            if (oldMapper != null)
                log.warn("实体类[" + genericType.getName() + "]存在多个CustomMapper实现类，[" + oldMapper.getClass().getName() + "]将被覆盖");

            mapperMap.put(genericType, mapper);
        }

        for (Entry<String, ServiceSupport> entry : serviceSupports.entrySet()) {
            ServiceSupport support = entry.getValue();
            Class<?> genericType = ReflectUtil.getSuperClassArgument(support.getClass(), ServiceSupport.class, 0);

            if (genericType == null || genericType == Object.class) {
                log.warn("[" + support.getClass().getName() + "]的实现类没有明确定义[" + ServiceSupport.class.getName() + "]的泛型，无法为其注册CustomMapper");
                continue;
            }

            CustomMapper mapper = mapperMap.get(genericType);
            if (mapper == null) {
                log.warn("实体类[" + genericType.getName() + "]没有对应的[" + CustomMapper.class.getName() + "]的实现类");
                continue;
            } else {
                support.setSqlMapper(mapper);
                log.info("===>为[" + support.getClass().getName() + "]注入CustomMapper");
            }

            support.init();
            log.info("<===[" + support.getClass().getName() + "]初始化成功");
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

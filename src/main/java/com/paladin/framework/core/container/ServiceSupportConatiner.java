package com.paladin.framework.core.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.paladin.framework.core.ServiceSupport;
import com.paladin.framework.core.ServiceSupportComplex;
import com.paladin.framework.core.configuration.mybatis.CustomJoinMapper;
import com.paladin.framework.core.configuration.mybatis.CustomMapper;
import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.spring.SpringContainer;
import com.paladin.framework.utils.reflect.ReflectUtil;

/**
 * 
 * 业务支持类容器，启动时为{@link com.paladin.service.ServiceSupport}自动注入SqlMapper
 * 
 * @author TontoZhou
 *
 */
@Component
public class ServiceSupportConatiner implements SpringContainer {

	private static Logger logger = LoggerFactory.getLogger(ServiceSupportConatiner.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean initialize() {

		/**
		 * 根据泛型类型为service support注入相应的sqlMapper
		 */

		Map<String, CustomMapper> cusomerMappers = SpringBeanHelper.getBeansByType(CustomMapper.class);
		Map<String, CustomJoinMapper> joinMappers = SpringBeanHelper.getBeansByType(CustomJoinMapper.class);
		Map<String, ServiceSupport> serviceSupports = SpringBeanHelper.getBeansByType(ServiceSupport.class);

		Map<Class<?>, CustomMapper> mapperMap = new HashMap<>();

		for (Entry<String, CustomMapper> entry : cusomerMappers.entrySet()) {
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

		Map<Class<?>, CustomJoinMapper> joinMapperMap = new HashMap<>();

		for (Entry<String, CustomJoinMapper> entry : joinMappers.entrySet()) {
			CustomJoinMapper mapper = entry.getValue();
			Class<?> genericType = ReflectUtil.getSuperClassArgument(mapper.getClass(), CustomJoinMapper.class, 0);

			if (genericType == null || genericType == Object.class) {
				logger.warn("[" + mapper.getClass().getName() + "]的实现类没有明确定义[" + CustomJoinMapper.class.getName() + "]的泛型");
				continue;
			}

			CustomJoinMapper oldMapper = joinMapperMap.get(genericType);
			if (oldMapper != null)
				logger.warn("实体类[" + genericType.getName() + "]存在多个JoinMapper实现类，[" + oldMapper.getClass().getName() + "]将被覆盖");

			joinMapperMap.put(genericType, mapper);
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

			if (support instanceof ServiceSupportComplex) {
				ServiceSupportComplex complexSupport = (ServiceSupportComplex) support;
				Class<?> joinGenericType = ReflectUtil.getSuperClassArgument(complexSupport.getClass(), ServiceSupportComplex.class, 1);

				if (genericType == null || genericType == Object.class) {
					logger.warn("[" + complexSupport.getClass().getName() + "]的实现类没有明确定义[" + ServiceSupportComplex.class.getName() + "]的泛型，无法为其注册JoinMapper");
					continue;
				}

				CustomJoinMapper joinMapper = joinMapperMap.get(joinGenericType);

				if (joinMapper == null) {
					logger.warn("实体类[" + joinGenericType.getName() + "]没有对应的[" + CustomJoinMapper.class.getName() + "]的实现类");
				} else {
					complexSupport.setJoinMapper(joinMapper);
					logger.info("===>为[" + complexSupport.getClass().getName() + "]注入JoinMapper<===");
				}
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

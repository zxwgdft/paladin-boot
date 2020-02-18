package com.paladin.framework.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 
 * @author TontoZhou
 *
 */
public class SpringContainerManager implements ApplicationListener<ContextRefreshedEvent> {

	private final static Logger logger = LoggerFactory.getLogger(SpringContainerManager.class);

	private boolean initialized = false;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextEvent) {

		// 由于web项目会存在两个容器导致该事件发生两次，web容器后发生所以这里选用后发生的有父容器的
		if (!initialized) {

			Map<String, SpringContainer> map = SpringBeanHelper.getBeansByType(SpringContainer.class);

			List<SpringContainer> containers = new ArrayList<>( map.values());
			
			// 排序
			Collections.sort(containers, new Comparator<SpringContainer>() {

				@Override
				public int compare(SpringContainer o1, SpringContainer o2) {
					return o1.order() - o2.order();
				}
				
			});
			
			
			for (SpringContainer container : containers) {

				logger.info("===>container[" + container.getClass() + "] begin to initialize<===");

				if (container.initialize())
					logger.info("===>container[" + container.getClass() + "] initialized successfully<===");
				else
					logger.warn("===>container[" + container.getClass() + "] initialized failed<===");
			}

			for (SpringContainer container : containers) {

				logger.info("===>container[" + container.getClass()
						+ "] begin to start after initialized successfully<===");

				if (container.afterInitialize())
					logger.info(
							"===>container[" + container.getClass() + "] started after initialized successfully<===");
				else
					logger.warn("===>container[" + container.getClass() + "] started after initialized failed<===");
			}

			initialized = true;
		}
	}

}

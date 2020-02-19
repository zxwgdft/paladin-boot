package com.paladin.framework.spring;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author TontoZhou
 */
@Slf4j
@Component
public class SpringContainerManager implements ApplicationListener<ContextRefreshedEvent> {

    private boolean initialized = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextEvent) {

        // 由于web项目会存在两个容器导致该事件发生两次，web容器后发生所以这里选用后发生的有父容器的
        if (!initialized) {

            Map<String, SpringContainer> map = SpringBeanHelper.getBeansByType(SpringContainer.class);

            List<SpringContainer> containers = new ArrayList<>(map.values());

            // 排序
            Collections.sort(containers, (SpringContainer o1, SpringContainer o2) -> o1.order() - o2.order());

            for (SpringContainer container : containers) {
                log.info("===>container[" + container.getClass() + "] begin to initialize<===");
                if (container.initialize()) {
                    log.info("===>container[" + container.getClass() + "] initialized successfully<===");
                } else {
                    log.warn("===>container[" + container.getClass() + "] initialized failed<===");
                }
            }

            for (SpringContainer container : containers) {
                log.info("===>container[" + container.getClass()
                        + "] begin to start after initialized successfully<===");

                if (container.afterInitialize()) {
                    log.info(
                            "===>container[" + container.getClass() + "] started after initialized successfully<===");
                } else {
                    log.warn("===>container[" + container.getClass() + "] started after initialized failed<===");
                }
            }

            initialized = true;
        }
    }

}

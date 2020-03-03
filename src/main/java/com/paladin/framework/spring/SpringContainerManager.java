package com.paladin.framework.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author TontoZhou
 */
@Slf4j
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

            log.info("===>开始初始化SpringContainer");

            for (SpringContainer container : containers) {
                if (container.initialize()) {
                    log.info("<===[" + container.getClass() + "]初始化成功");
                } else {
                    log.warn("<===[" + container.getClass() + "]初始化失败");
                }
            }

            for (SpringContainer container : containers) {
                if (container.afterInitialize()) {
                    log.info("<===[" + container.getClass() + "]启动后初始化成功");
                } else {
                    log.warn("<===[" + container.getClass() + "]启动后初始化失败");
                }
            }

            initialized = true;
        }
    }

}

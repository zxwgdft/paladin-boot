package com.paladin.framework.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author TontoZhou
 */
@Slf4j
public class SpringContainerManager implements CommandLineRunner {

    private boolean initialized = false;

    @Override
    public void run(String... args) {

        // 由于web项目会存在两个容器导致该事件发生两次，web容器后发生所以这里选用后发生的有父容器的
        if (!initialized) {

            Map<String, SpringContainer> map = SpringBeanHelper.getBeansByType(SpringContainer.class);

            List<SpringContainer> containers = new ArrayList<>(map.values());

            // 排序
            Collections.sort(containers, (SpringContainer o1, SpringContainer o2) -> o1.order() - o2.order());

            log.info("===>开始初始化SpringContainer");

            for (SpringContainer container : containers) {
                if (container.initialize()) {
                    log.debug("<===[" + container.getClass() + "]初始化成功");
                } else {
                    log.warn("<===[" + container.getClass() + "]初始化失败");
                }
            }

            for (SpringContainer container : containers) {
                if (container.afterInitialize()) {
                    log.debug("<===[" + container.getClass() + "]启动后初始化成功");
                } else {
                    log.warn("<===[" + container.getClass() + "]启动后初始化失败");
                }
            }

            log.info("===>初始化SpringContainer结束");

            initialized = true;
        }
    }

}

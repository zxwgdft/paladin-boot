package com.paladin.framework.spring;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class NotProduceCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        return isNotProduce(environment);
    }

    public static boolean isNotProduce(Environment environment) {
        String[] actives = environment.getActiveProfiles();
        for (String active : actives) {
            if ("pro".equals(active)) {
                return false;
            }
        }
        return true;
    }

}

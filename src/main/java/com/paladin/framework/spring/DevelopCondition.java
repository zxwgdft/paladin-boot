package com.paladin.framework.spring;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class DevelopCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        return isDevelop(environment);
    }

    public static boolean isDevelop(Environment environment) {
        String[] actives = environment.getActiveProfiles();
        for (String active : actives) {
            if ("dev".equals(active)) {
                return true;
            }
        }
        return false;
    }


}

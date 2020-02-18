package com.paladin.framework.spring;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class DevCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String active = context.getEnvironment().getProperty("spring.profiles.active");
		String[] aa = active.split(",");
		for (String a : aa) {
			if ("dev".equals(a)) {
				return true;
			}
		}
		return false;
	}

}

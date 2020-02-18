package com.paladin.framework.core;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.paladin.framework.core.query.QueryOutputMethod;
import com.paladin.framework.utils.JsonUtil;

@Aspect
@Component
public class SpringMethodInterceptor {
	
	 @Before("@annotation(queryMethod)")
	 public void beforeQuery(JoinPoint point, QueryOutputMethod queryMethod) {  
		 Session session = SecurityUtils.getSubject().getSession();		 
		 Object param = point.getArgs()[queryMethod.paramIndex()];
		 session.setAttribute(queryMethod.queryClass().getName(), JsonUtil.getJson(param));
	 }
	
}

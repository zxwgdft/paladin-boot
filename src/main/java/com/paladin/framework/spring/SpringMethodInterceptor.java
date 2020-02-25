package com.paladin.framework.spring;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.paladin.framework.service.QueryOutputMethod;
import com.paladin.framework.utils.convert.JsonUtil;

import java.io.IOException;

@Aspect
@Component
public class SpringMethodInterceptor {
	
	 @Before("@annotation(queryMethod)")
	 public void beforeQuery(JoinPoint point, QueryOutputMethod queryMethod) throws IOException {
		 Session session = SecurityUtils.getSubject().getSession();		 
		 Object param = point.getArgs()[queryMethod.paramIndex()];
		 session.setAttribute(queryMethod.queryClass().getName(), JsonUtil.getJson(param));
	 }
	
}

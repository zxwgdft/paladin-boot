package com.paladin.framework.service;

import com.paladin.framework.service.annotation.QueryOutputMethod;
import com.paladin.framework.utils.convert.JsonUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.io.IOException;

@Aspect
public class QueryMethodInterceptor {

    @Before("@annotation(queryMethod)")
    public void beforeQuery(JoinPoint point, QueryOutputMethod queryMethod) throws IOException {
        Session session = SecurityUtils.getSubject().getSession();
        Object param = point.getArgs()[queryMethod.paramIndex()];
        session.setAttribute(queryMethod.queryClass().getName(), JsonUtil.getJson(param));
    }

}

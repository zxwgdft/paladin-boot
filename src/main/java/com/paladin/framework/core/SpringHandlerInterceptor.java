package com.paladin.framework.core;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.paladin.framework.core.query.QueryInputMethod;
import com.paladin.framework.utils.JsonUtil;

public class SpringHandlerInterceptor implements HandlerInterceptor {

	private HashMap<HandlerMethod, QueryMethodShell> cacheMap = new HashMap<>();

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

		if (handler instanceof HandlerMethod && modelAndView != null) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;

			QueryMethodShell shell = cacheMap.get(handlerMethod);

			if (shell == null) {
				synchronized (SpringHandlerInterceptor.class) {
					shell = cacheMap.get(handlerMethod);
					if (shell == null) {
						QueryInputMethod queryMethod = handlerMethod.getMethodAnnotation(QueryInputMethod.class);
						shell = new QueryMethodShell(queryMethod);
						cacheMap.put(handlerMethod, shell);
					}
				}
			}

			if (shell.queryMethod != null) {
				QueryInputMethod queryMethod = shell.queryMethod;
				Object object = SecurityUtils.getSubject().getSession().getAttribute(queryMethod.queryClass().getName());
				if (object != null) {
					if(object instanceof String) {
						object = JsonUtil.parseJson((String)object, queryMethod.queryClass());
					}
					modelAndView.addObject(queryMethod.viewName(), object);
				}
			}

		}
	}

	private static class QueryMethodShell {
		QueryInputMethod queryMethod;

		private QueryMethodShell(QueryInputMethod queryMethod) {
			this.queryMethod = queryMethod;
		}
	}

}

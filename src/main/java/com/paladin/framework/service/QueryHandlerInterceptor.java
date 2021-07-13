package com.paladin.framework.service;

import com.paladin.framework.service.annotation.QueryInputMethod;
import com.paladin.framework.utils.convert.JsonUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueryHandlerInterceptor implements HandlerInterceptor {

    private volatile Map<HandlerMethod, QueryMethodShell> cacheMap = new ConcurrentHashMap<>();

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod && modelAndView != null) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            QueryMethodShell shell = cacheMap.get(handlerMethod);

            if (shell == null) {
                synchronized (QueryHandlerInterceptor.class) {
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
                    if (object instanceof String) {
                        object = JsonUtil.parseJson((String) object, queryMethod.queryClass());
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

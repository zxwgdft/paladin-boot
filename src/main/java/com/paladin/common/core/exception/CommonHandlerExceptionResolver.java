package com.paladin.common.core.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paladin.framework.common.HttpCode;
import com.paladin.framework.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CommonHandlerExceptionResolver implements HandlerExceptionResolver {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethodAnnotation(ResponseBody.class) != null) {
                MappingJackson2JsonView jsonView = new MappingJackson2JsonView(objectMapper);
                jsonView.addStaticAttribute("message", ex.getMessage());
                jsonView.addStaticAttribute("success", false);
                jsonView.addStaticAttribute("code", HttpCode.FAILURE);

                if (ex instanceof SystemException) {
                    log.error("异常", ex);
                }

                return new ModelAndView(jsonView);
            } else {
                return new ModelAndView("/common/error/error", "errorMessage", ex.getMessage());
            }
        }

        return null;
    }

}

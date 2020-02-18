package com.paladin.common.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paladin.framework.core.exception.BusinessException;
import com.paladin.framework.web.response.Response;

public class CommonHandlerExceptionResolver implements HandlerExceptionResolver {

	private static Logger logger = LoggerFactory.getLogger(CommonHandlerExceptionResolver.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getMethodAnnotation(ResponseBody.class) != null) {
				MappingJackson2JsonView jsonView = new MappingJackson2JsonView(objectMapper);
				jsonView.addStaticAttribute("message", ex.getMessage());

				if (ex instanceof BusinessException) {
					jsonView.addStaticAttribute("status", Response.STATUS_FAIL);
				} else {
					jsonView.addStaticAttribute("status", Response.STATUS_ERROR);
					logger.error("异常", ex);
				}

				return new ModelAndView(jsonView);
			} else {
				return new ModelAndView("/common/error/error", "errorMessage", ex.getMessage());
			}
		}

		return null;
	}

}

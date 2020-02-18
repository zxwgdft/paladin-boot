package com.paladin.framework.core;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import com.paladin.framework.core.copy.SimpleBeanCopier.SimpleBeanCopyUtil;
import com.paladin.framework.web.response.CommonResponse;

public class ControllerSupport {

	/** 成功 */
	public final static CommonResponse SUCCESS = CommonResponse.getSuccessResponse();
	/** 失败，业务处理失败，由业务逻辑引起 */
	public final static CommonResponse FAIL = CommonResponse.getFailResponse();
	/** 异常，通常指前端传入参数异常或系统异常，非业务逻辑引起 */
	public final static CommonResponse ERROR = CommonResponse.getErrorResponse();

	/**
	 * 验证异常处理
	 * 
	 * @param bindingResult
	 * @return
	 */
	public Object validErrorHandler(BindingResult bindingResult) {

		List<FieldError> errors = bindingResult.getFieldErrors();

		String[][] result = new String[errors.size()][3];

		int i = 0;

		for (FieldError error : bindingResult.getFieldErrors()) {
			result[i++] = new String[] { error.getCode(), error.getField(), error.getDefaultMessage() };
		}

		return CommonResponse.getValidFailResponse(result);
	}

	protected <T> T beanCopy(Object source, T target) {
		SimpleBeanCopyUtil.simpleCopy(source, target, true);
		return target;
	}

	public <T> List<T> beanCopyList(List<?> sourceList, Class<T> targetType) {
		return SimpleBeanCopyUtil.simpleCopyList(sourceList, targetType);
	}

	public <T> List<T> beanCopyList(List<?> sourceList, List<T> targeList) {
		return SimpleBeanCopyUtil.simpleCopyList(sourceList, targeList);
	}
	
	public ModelAndView returnErrorView(String errorMessage) {
		return new ModelAndView("/common/error/error", "errorMessage", errorMessage);
	}
}

package com.paladin.common.core;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class ControllerSupport {

    /**
     * 验证异常处理
     *
     * @param bindingResult
     */
    public void validErrorHandler(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();

            String[] result = new String[errors.size()];

            int i = 0;

            for (FieldError error : bindingResult.getFieldErrors()) {
                result[i++] = error.getDefaultMessage();
            }

            throw new BusinessException("参数验证失败", result);
        }
    }

    protected <T> T beanCopy(Object source, T target) {
        return SimpleBeanCopyUtil.simpleCopy(source, target, null);
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

package com.paladin.common.core;

import com.paladin.framework.common.HttpCode;
import com.paladin.framework.common.R;
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
     * @return
     */
    public Object validErrorHandler(BindingResult bindingResult) {

        List<FieldError> errors = bindingResult.getFieldErrors();

        String[][] result = new String[errors.size()][3];

        int i = 0;

        for (FieldError error : bindingResult.getFieldErrors()) {
            result[i++] = new String[]{error.getCode(), error.getField(), error.getDefaultMessage()};
        }

        return R.fail(HttpCode.BAD_REQUEST, "参数验证失败", result);
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

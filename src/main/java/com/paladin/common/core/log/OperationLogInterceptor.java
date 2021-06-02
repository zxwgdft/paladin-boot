package com.paladin.common.core.log;

import com.paladin.common.model.sys.SysLoggerOperate;
import com.paladin.common.service.sys.SysLoggerOperateService;
import com.paladin.framework.api.R;
import com.paladin.framework.service.UserSession;
import com.paladin.framework.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author TontoZhou
 * @since 2020/6/15
 */
@Aspect
@Slf4j
public class OperationLogInterceptor {

    @Autowired
    private SysLoggerOperateService loggerOperateService;


    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint point, OperationLog operationLog) throws Throwable {

        MethodInvocationProceedingJoinPoint methodPoint = (MethodInvocationProceedingJoinPoint) point;

        Class<?> clazz = methodPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) methodPoint.getSignature();

        String className = clazz.getName();
        String methodName = methodSignature.getMethod().getName();
        String modelName = operationLog.model().length() > 0 ? operationLog.model() : clazz.getSimpleName();
        String operateName = operationLog.operate().length() > 0 ? operationLog.operate() : methodName;

        UserSession session = UserSession.getCurrentUserSession();

        SysLoggerOperate operate = new SysLoggerOperate();
        operate.setId(UUIDUtil.createUUID());
        operate.setClassName(cut(className, 100));
        operate.setMethodName(methodName);
        operate.setModelName(modelName);
        operate.setOperateName(operateName);

        operate.setOperateBy(session.getUserId());
        operate.setOperateByName(session.getUserName());
        operate.setOperateTime(new Date());

        long time = System.currentTimeMillis();

        try {
            Object returnValue = point.proceed();
            if (returnValue instanceof R) {
                R r = (R) returnValue;
                if (r.isSuccess()) {
                    operate.setIsSuccess(true);
                } else {
                    operate.setIsSuccess(false);
                    operate.setErrorMessage(cut(r.getMessage(), 255));
                }
            } else {
                operate.setIsSuccess(true);
            }
            return returnValue;
        } catch (Throwable throwable) {
            operate.setIsSuccess(false);
            operate.setErrorMessage(cut(throwable.getMessage(), 255));
            throw throwable;
        } finally {
            operate.setOperateDuration((int) (System.currentTimeMillis() - time));
            loggerOperateService.save(operate);
        }
    }

    private String cut(String str, int length) {
        if (str != null && str.length() > length) {
            return str.substring(0, length);
        }
        return str;
    }

}

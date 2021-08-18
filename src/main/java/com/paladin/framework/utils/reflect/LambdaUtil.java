package com.paladin.framework.utils.reflect;

import com.paladin.framework.exception.SystemException;
import com.paladin.framework.utils.support.GetFunction;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TontoZhou
 * @since 2021/3/17
 */
@Slf4j
public class LambdaUtil {

    private static Map<String, String> fieldNameCache = new ConcurrentHashMap<>();

    /**
     * 通过get function获取字段名称
     *
     * @param fun get function
     * @return get方法对应字段名称
     */
    public static <T> String getFieldNameByFunction(GetFunction<T> fun) {
        String funName = fun.getClass().getName();
        String fieldName = fieldNameCache.get(funName);
        if (fieldName == null) {
            try {
                Method method = fun.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fun);
                String getName = serializedLambda.getImplMethodName();
                fieldName = NameUtil.removeGetOrSet(getName);
                fieldNameCache.put(funName, fieldName);
            } catch (Exception e) {
                throw new SystemException(SystemException.CODE_ERROR_CODE, String.format("get field name error from [%s] by lambda", fun.toString()), e);
            }
        }
        return fieldName;
    }

}

package com.paladin.framework.common;

import com.paladin.framework.exception.SystemException;
import com.paladin.framework.exception.SystemExceptionCode;

/**
 * @author TontoZhou
 * @since 2020/1/2
 */
public interface CodeEnum {

    int getCode();

    String getName();

    static <E extends Enum<?>> E codeOf(Class<E> enumClass, int code) {
        if (CodeEnum.class.isAssignableFrom(enumClass)) {
            E[] enumConstants = enumClass.getEnumConstants();
            for (E e : enumConstants) {
                if (((CodeEnum) e).getCode() == code)
                    return e;
            }
            return null;
        } else {
            throw new SystemException(SystemExceptionCode.CODE_ERROR_CODE, "不支持的枚举类型");
        }
    }
}

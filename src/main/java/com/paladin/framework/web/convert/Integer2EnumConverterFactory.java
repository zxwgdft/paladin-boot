package com.paladin.framework.web.convert;

import com.paladin.framework.common.CodeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @author TontoZhou
 * @since 2020/1/3
 */
public class Integer2EnumConverterFactory extends CachedConverterFactory<Integer, Enum> {

    @Override
    public <T extends Enum> Converter<Integer, T> doGetConverter(Class<T> targetType) {
        if (CodeEnum.class.isAssignableFrom(targetType)) {
            return new IntegerToCodeEnum(getEnumType(targetType));
        } else {
            return new IntegerToEnum(getEnumType(targetType));
        }
    }

    private static class IntegerToCodeEnum<T extends Enum> implements Converter<Integer, T> {

        private final Class<T> enumType;

        public IntegerToCodeEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(Integer source) {
            return (T) CodeEnum.codeOf(enumType, source);
        }
    }

    private static class IntegerToEnum<T extends Enum> implements Converter<Integer, T> {

        private final Class<T> enumType;

        public IntegerToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(Integer source) {
            return this.enumType.getEnumConstants()[source];
        }
    }

    private Class<?> getEnumType(Class<?> targetType) {
        Class<?> enumType = targetType;
        while (enumType != null && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }
        return enumType;
    }

}

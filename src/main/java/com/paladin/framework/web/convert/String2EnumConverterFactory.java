package com.paladin.framework.web.convert;

import com.paladin.framework.common.CodeEnum;
import org.springframework.core.convert.converter.Converter;

/**
 * @author TontoZhou
 * @since 2020/1/3
 */
public class String2EnumConverterFactory extends CachedConverterFactory<String, Enum> {

    @Override
    public <T extends Enum> Converter<String, T> doGetConverter(Class<T> targetType) {
        if (CodeEnum.class.isAssignableFrom(targetType)) {
            return new StringToCodeEnum(getEnumType(targetType));
        } else {
            return new StringToEnum(getEnumType(targetType));
        }
    }

    private static class StringToCodeEnum<T extends Enum> implements Converter<String, T> {

        private final Class<T> enumType;

        public StringToCodeEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (!source.isEmpty()) {
                try {
                    Integer code = Integer.valueOf(source.trim());
                    return (T) CodeEnum.codeOf(enumType, code);
                } catch (Exception e) {
                }
            }
            return null;
        }
    }

    private static class StringToEnum<T extends Enum> implements Converter<String, T> {

        private final Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }
            return (T) Enum.valueOf(this.enumType, source.trim());
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

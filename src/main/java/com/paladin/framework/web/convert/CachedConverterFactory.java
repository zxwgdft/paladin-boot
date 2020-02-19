package com.paladin.framework.web.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2020/1/3
 */
public abstract class CachedConverterFactory<S, R> implements ConverterFactory<S, R> {

    private Map<Class, Converter> convertMap = new HashMap<>();

    @Override
    public <T extends R> Converter<S, T> getConverter(Class<T> targetType) {
        Converter convert = convertMap.get(targetType);
        if (convert == null) {
            synchronized (convertMap) {
                convert = convertMap.get(targetType);
                if (convert == null) {
                    convert = doGetConverter(targetType);
                    convertMap.put(targetType, convert);
                }
            }
        }
        return convert;
    }

    public abstract <T extends R> Converter<S, T> doGetConverter(Class<T> targetType);

}

package com.paladin.framework.utils.support;

import java.io.Serializable;

/**
 * @author TontoZhou
 * @since 2021/3/17
 */
@FunctionalInterface
public interface SetFunction<T, V> extends Serializable {
    void accept(T t, V v);
}

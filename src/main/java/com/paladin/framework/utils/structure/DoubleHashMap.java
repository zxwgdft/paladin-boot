package com.paladin.framework.utils.structure;

import java.util.HashMap;
import java.util.Map;

/**
 * 双主键用HashMap
 *
 * @param <K1>
 * @param <K2>
 * @param <V>
 */
public class DoubleHashMap<K1, K2, V> {

    private Map<K1, Map<K2, V>> secMap = new HashMap<>();

    public V get(K1 key1, K2 key2) {
        Map<K2, V> map = secMap.get(key1);
        if (map != null)
            return map.get(key2);
        return null;
    }

    public void put(K1 key1, K2 key2, V value) {
        Map<K2, V> map = secMap.get(key1);
        if (map == null) {
            map = new HashMap<>();
            secMap.put(key1, map);
        }

        map.put(key2, value);
    }

    public V remove(K1 key1, K2 key2) {
        Map<K2, V> map = secMap.get(key1);
        if (map != null) {
            V val = map.remove(key2);
            if (map.size() == 0)
                secMap.remove(key1);
            return val;
        }

        return null;
    }

    public Map<K1, Map<K2, V>> getSource() {
        return secMap;
    }

}

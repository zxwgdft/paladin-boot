package com.paladin.common.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstantsContainer {

    private Map<String, List<KeyValue>> constantMap = new HashMap<>();
    private Map<String, Map<String, String>> constantValueMap = new HashMap<>();
    private Map<String, Map<String, String>> constantKeyMap = new HashMap<>();

    public ConstantsContainer(Map<String, List<KeyValue>> constantMap,
                              Map<String, Map<String, String>> constantValueMap,
                              Map<String, Map<String, String>> constantKeyMap) {
        this.constantMap = constantMap;
        this.constantKeyMap = constantKeyMap;
        this.constantValueMap = constantValueMap;
    }

    /**
     * 获取类型常量
     *
     * @param typeCodes
     * @return
     */
    public Map<String, List<KeyValue>> getTypeConstants(String... typeCodes) {
        Map<String, List<KeyValue>> result = new HashMap<>();
        for (String typeCode : typeCodes) {
            result.put(typeCode, constantMap.get(typeCode));
        }
        return result;
    }

    /**
     * 获取类型常量
     *
     * @param typeCode
     * @return
     */
    public List<KeyValue> getTypeConstant(String typeCode) {
        return constantMap.get(typeCode);
    }


    /**
     * 根据类型和名称得到常量KEY
     *
     * @param type
     * @param name
     * @return
     */
    public String getTypeKey(String type, String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        Map<String, String> valueMap = constantValueMap.get(type);
        if (valueMap != null) {
            return valueMap.get(name);
        }
        return null;
    }

    /**
     * 根据类型和编码得到常量值
     *
     * @param type
     * @param key
     * @return
     */
    public String getTypeValue(String type, String key) {
        if (key == null) {
            return null;
        }
        Map<String, String> keyMap = constantKeyMap.get(type);
        if (keyMap != null) {
            return keyMap.get(key);
        }
        return null;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public final static class KeyValue {
        private String key;
        private String value;
    }

}

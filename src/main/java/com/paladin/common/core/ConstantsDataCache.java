package com.paladin.common.core;

import com.paladin.common.mapper.sys.SysConstantMapper;
import com.paladin.common.model.sys.SysConstant;
import com.paladin.framework.cache.DataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConstantsDataCache implements DataCache<ConstantsContainer> {

    @Autowired
    private SysConstantMapper sysConstantMapper;

    public String getId() {
        return "CONSTANTS_CACHE";
    }

    @Override
    public ConstantsContainer loadData(long version) {

        Map<String, List<ConstantsContainer.KeyValue>> enumConstantMap = new HashMap<>();
        Map<String, Map<String, String>> constantValueMap = new HashMap<>();
        Map<String, Map<String, String>> constantKeyMap = new HashMap<>();

        List<SysConstant> constants = sysConstantMapper.findList();
        for (SysConstant constant : constants) {
            String type = constant.getType();
            List<ConstantsContainer.KeyValue> kvList = enumConstantMap.get(type);
            if (kvList == null) {
                kvList = new ArrayList<>();
                enumConstantMap.put(type, kvList);
            }

            String code = constant.getCode();
            String name = constant.getName();

            kvList.add(new ConstantsContainer.KeyValue(code, name));

            Map<String, String> valueMap = constantValueMap.get(type);
            if (valueMap == null) {
                valueMap = new HashMap<>();
                constantValueMap.put(type, valueMap);
            }

            valueMap.put(name, code);

            Map<String, String> keyMap = constantKeyMap.get(type);
            if (keyMap == null) {
                keyMap = new HashMap<>();
                constantKeyMap.put(type, keyMap);
            }

            keyMap.put(code, name);
        }

        return new ConstantsContainer(enumConstantMap, constantValueMap, constantKeyMap);
    }


}

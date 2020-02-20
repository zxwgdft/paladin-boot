package com.paladin.data.generate;

import com.paladin.framework.common.BaseModel;

import java.util.HashSet;
import java.util.Set;

public class GenerateEnvironment {

    public static final Set<Class<?>> baseModelTypeMap = new HashSet<>();

    static {
        baseModelTypeMap.add(BaseModel.class);
    }


}

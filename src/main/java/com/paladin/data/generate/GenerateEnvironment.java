package com.paladin.data.generate;

import com.paladin.framework.api.BaseModel;
import com.paladin.framework.api.DeletedBaseModel;

import java.util.HashSet;
import java.util.Set;

public class GenerateEnvironment {

    public static final Set<Class<?>> baseModelTypeMap = new HashSet<>();

    static {
        baseModelTypeMap.add(BaseModel.class);
        baseModelTypeMap.add(DeletedBaseModel.class);
    }


}

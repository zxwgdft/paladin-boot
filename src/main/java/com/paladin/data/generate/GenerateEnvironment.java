package com.paladin.data.generate;

import java.util.HashSet;
import java.util.Set;

import com.paladin.framework.common.BaseModel;
import com.paladin.framework.common.UnDeleteBaseModel;
import com.paladin.framework.common.UnDeleteModel;

public class GenerateEnvironment {
	
	//public static final Map<Class<?>, String> regularTypeMap = new HashMap<>();
	public static final Set<Class<?>> baseModelTypeMap = new HashSet<>();
	
	
	static {
		//regularTypeMap.put(Date.class, "date");	
		
		baseModelTypeMap.add(BaseModel.class);
		baseModelTypeMap.add(UnDeleteModel.class);
		baseModelTypeMap.add(UnDeleteBaseModel.class);

	}
	
//	public static String getRegularType(Class<?> type) {
//		return regularTypeMap.get(type);
//	}
//	
	
	
}

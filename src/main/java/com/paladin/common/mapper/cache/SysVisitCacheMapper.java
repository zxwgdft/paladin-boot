package com.paladin.common.mapper.cache;

import org.apache.ibatis.annotations.Param;

import com.paladin.common.model.cache.SysVisitCache;
import com.paladin.framework.core.configuration.mybatis.CustomMapper;

public interface SysVisitCacheMapper extends CustomMapper<SysVisitCache>{
	
	public SysVisitCache getCache(@Param("key") String key, @Param("ip") String ip);
	
}
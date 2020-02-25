package com.paladin.common.mapper.cache;

import com.paladin.common.model.cache.SysVisitCache;
import com.paladin.framework.mybatis.CustomMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysVisitCacheMapper extends CustomMapper<SysVisitCache> {

    @Select("SELECT * FROM sys_visit_cache WHERE code = #{key} AND ip = #{ip}")
    SysVisitCache getCache(@Param("key") String key, @Param("ip") String ip);

}
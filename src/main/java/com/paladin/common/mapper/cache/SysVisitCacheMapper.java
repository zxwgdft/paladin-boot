package com.paladin.common.mapper.cache;

import org.apache.ibatis.annotations.Param;

import com.paladin.common.model.cache.SysVisitCache;

public interface SysVisitCacheMapper extends tk.mybatis.mapper.common.base.select.SelectAllMapper<SysVisitCache>, tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper<SysVisitCache>, tk.mybatis.mapper.common.base.insert.InsertMapper<SysVisitCache>, tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper<SysVisitCache>, tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper<SysVisitCache>, tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper<SysVisitCache>, tk.mybatis.mapper.common.example.DeleteByExampleMapper<SysVisitCache>, tk.mybatis.mapper.common.example.SelectOneByExampleMapper<SysVisitCache>, tk.mybatis.mapper.common.example.SelectByExampleMapper<SysVisitCache>, tk.mybatis.mapper.common.example.SelectCountByExampleMapper<SysVisitCache>, tk.mybatis.mapper.common.example.UpdateByExampleMapper<SysVisitCache>, tk.mybatis.mapper.common.example.UpdateByExampleSelectiveMapper<SysVisitCache>, tk.mybatis.mapper.common.Marker {
	
	public SysVisitCache getCache(@Param("key") String key, @Param("ip") String ip);
	
}
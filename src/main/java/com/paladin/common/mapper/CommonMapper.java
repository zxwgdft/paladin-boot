package com.paladin.common.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

public interface CommonMapper {

	int saveContainerVersion(@Param("containerId") String containerId, @Param("version") Long version, @Param("updateTime") Date updateTime);

	int updateContainerVersion(@Param("containerId") String containerId, @Param("originVersion") Long originVersion, @Param("currentVersion") Long currentVersion, @Param("updateTime") Date updateTime);
	
	Long getContainerVersion(@Param("containerId") String containerId);
	
}

package com.paladin.framework.service;

import java.util.Date;

/**
 * 版本容器数据持久化接口
 * @author TontoZhou
 * @since 2018年3月22日
 */
public interface VersionContainerDAO {
	
	Long getVersion(String containerId) ;
	
	int saveContainer(String containerId, Long version, Date updateTime);
	
	int updateVersion(String containerId, Long originVersion, Long currentVersion, Date updateTime);
	
	
}

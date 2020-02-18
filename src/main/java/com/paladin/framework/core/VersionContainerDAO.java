package com.paladin.framework.core;

import java.util.Date;

/**
 * 版本容器数据持久化接口
 * @author TontoZhou
 * @since 2018年3月22日
 */
public interface VersionContainerDAO {
	
	public Long getVersion(String containerId) ;
	
	public int saveContainer(String containerId, Long version, Date updateTime);
	
	public int updateVersion(String containerId, Long originVersion, Long currentVersion, Date updateTime);
	
	
}

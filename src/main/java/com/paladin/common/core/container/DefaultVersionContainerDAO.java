package com.paladin.common.core.container;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.paladin.common.mapper.CommonMapper;
import com.paladin.framework.core.VersionContainerDAO;

public class DefaultVersionContainerDAO implements VersionContainerDAO{

	@Autowired
	private CommonMapper commonMapper;
	
	@Override
	public Long getVersion(String containerId) {
		return commonMapper.getContainerVersion(containerId);
	}

	@Override
	public int saveContainer(String containerId, Long version, Date updateTime) {
		return commonMapper.saveContainerVersion(containerId, version, updateTime);
	}

	@Override
	public int updateVersion(String containerId, Long originVersion, Long currentVersion, Date updateTime) {
		return commonMapper.updateContainerVersion(containerId, originVersion, currentVersion, updateTime);
	}

}

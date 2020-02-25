package com.paladin.common.core;

import com.paladin.common.mapper.CommonMapper;
import com.paladin.framework.service.VersionContainerDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class DefaultVersionContainerDAO implements VersionContainerDAO {

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

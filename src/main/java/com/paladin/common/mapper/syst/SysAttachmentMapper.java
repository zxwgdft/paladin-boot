package com.paladin.common.mapper.syst;

import java.util.List;

import com.paladin.common.model.syst.SysAttachment;
import com.paladin.framework.core.configuration.mybatis.CustomMapper;

public interface SysAttachmentMapper extends CustomMapper<SysAttachment>{

	public List<SysAttachment> findImageResource();

}
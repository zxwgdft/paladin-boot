package com.paladin.common.mapper.sys;

import com.paladin.common.model.sys.SysAttachment;
import com.paladin.framework.mybatis.CustomMapper;

import java.util.List;

public interface SysAttachmentMapper extends CustomMapper<SysAttachment> {

    List<SysAttachment> findImageResource();

}
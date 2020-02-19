package com.paladin.common.mapper.syst;

import java.util.List;

import com.paladin.common.model.syst.SysAttachment;

public interface SysAttachmentMapper extends tk.mybatis.mapper.common.base.select.SelectAllMapper<SysAttachment>, tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper<SysAttachment>, tk.mybatis.mapper.common.base.insert.InsertMapper<SysAttachment>, tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper<SysAttachment>, tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper<SysAttachment>, tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper<SysAttachment>, tk.mybatis.mapper.common.example.DeleteByExampleMapper<SysAttachment>, tk.mybatis.mapper.common.example.SelectOneByExampleMapper<SysAttachment>, tk.mybatis.mapper.common.example.SelectByExampleMapper<SysAttachment>, tk.mybatis.mapper.common.example.SelectCountByExampleMapper<SysAttachment>, tk.mybatis.mapper.common.example.UpdateByExampleMapper<SysAttachment>, tk.mybatis.mapper.common.example.UpdateByExampleSelectiveMapper<SysAttachment>, tk.mybatis.mapper.common.Marker {

	public List<SysAttachment> findImageResource();

}
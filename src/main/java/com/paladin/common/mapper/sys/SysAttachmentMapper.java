package com.paladin.common.mapper.sys;

import com.paladin.common.model.sys.SysAttachment;
import com.paladin.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface SysAttachmentMapper extends CommonMapper<SysAttachment> {

    int deleteAttachments(@Param("ids") String[] ids);

    int persistAttachments(@Param("ids") String[] ids, @Param("ownUserId") String ownUserId);

    @Delete("UPDATE sys_attachment SET deleted = 1, operate_time = NOW() WHERE operate_by = #{id}")
    int deleteAttachmentsByUser(@Param("id") String id);
}
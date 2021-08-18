package com.paladin.common.mapper.org;

import com.paladin.common.core.security.CodePermission;
import com.paladin.common.model.org.OrgPermission;
import com.paladin.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrgPermissionMapper extends CommonMapper<OrgPermission> {


    @Select("SELECT id,`name`,`code`,is_admin isAdmin FROM org_permission")
    List<CodePermission> findPermission();
}
package com.paladin.common.mapper.org;

import com.paladin.common.core.security.Permission;
import com.paladin.common.model.org.OrgPermission;
import com.paladin.common.model.org.OrgRoleMenu;
import com.paladin.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrgPermissionMapper extends CommonMapper<OrgPermission> {


    @Select("SELECT id,`name`,`code` FROM org_permission")
    List<Permission> findPermission();
}
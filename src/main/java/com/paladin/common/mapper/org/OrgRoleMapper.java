package com.paladin.common.mapper.org;

import com.paladin.common.core.security.Role;
import com.paladin.common.model.org.OrgRole;
import com.paladin.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrgRoleMapper extends CommonMapper<OrgRole> {

    @Select("SELECT id, role_name AS name, role_level AS level, enable FROM org_role")
    List<Role> findList();
}
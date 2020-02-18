package com.paladin.common.mapper.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.paladin.common.model.org.OrgRolePermission;
import com.paladin.framework.core.configuration.mybatis.CustomMapper;

public interface OrgRolePermissionMapper extends CustomMapper<OrgRolePermission> {

	public List<String> getPermissionByRole(@Param("id") String id);

	public int removePermissionByRole(@Param("id") String id);

	public int insertByBatch(@Param("roleId") String roleId, @Param("permissionIds") String[] permissionIds);

}
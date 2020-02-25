package com.paladin.common.mapper.org;

import com.paladin.common.model.org.OrgRolePermission;
import com.paladin.framework.mybatis.CustomMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrgRolePermissionMapper extends CustomMapper<OrgRolePermission> {

    List<String> getPermissionByRole(@Param("id") String id);

    int removePermissionByRole(@Param("id") String id);

    int insertByBatch(@Param("roleId") String roleId, @Param("permissionIds") String[] permissionIds);
}
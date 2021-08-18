package com.paladin.common.mapper.org;

import com.paladin.common.model.org.OrgRolePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrgRolePermissionMapper {

    List<Integer> getPermissionByRole(@Param("id") int id);

    int removePermissionByRole(@Param("id") int id);

    int insertByBatch(@Param("roleId") int roleId, @Param("permissionIds") List<Integer> permissionIds);

    @Select("SELECT role_id AS roleId, permission_id AS permissionId FROM org_role_permission ORDER BY role_id")
    List<OrgRolePermission> findList();
}
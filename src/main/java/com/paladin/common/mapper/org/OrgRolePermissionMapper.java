package com.paladin.common.mapper.org;

import com.paladin.common.model.org.OrgRolePermission;
import com.paladin.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrgRolePermissionMapper {

    List<String> getPermissionByRole(@Param("id") String id);

    int removePermissionByRole(@Param("id") String id);

    int insertByBatch(@Param("roleId") String roleId, @Param("permissionIds") List<String> permissionIds);

    @Select("SELECT role_id AS roleId, permission_id AS permissionId FROM org_role_permission ORDER BY role_id")
    List<OrgRolePermission> findList();
}
package com.paladin.common.mapper.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.paladin.common.model.org.OrgRolePermission;

public interface OrgRolePermissionMapper extends tk.mybatis.mapper.common.base.select.SelectAllMapper<OrgRolePermission>, tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper<OrgRolePermission>, tk.mybatis.mapper.common.base.insert.InsertMapper<OrgRolePermission>, tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper<OrgRolePermission>, tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper<OrgRolePermission>, tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper<OrgRolePermission>, tk.mybatis.mapper.common.example.DeleteByExampleMapper<OrgRolePermission>, tk.mybatis.mapper.common.example.SelectOneByExampleMapper<OrgRolePermission>, tk.mybatis.mapper.common.example.SelectByExampleMapper<OrgRolePermission>, tk.mybatis.mapper.common.example.SelectCountByExampleMapper<OrgRolePermission>, tk.mybatis.mapper.common.example.UpdateByExampleMapper<OrgRolePermission>, tk.mybatis.mapper.common.example.UpdateByExampleSelectiveMapper<OrgRolePermission>, tk.mybatis.mapper.common.Marker {

	public List<String> getPermissionByRole(@Param("id") String id);

	public int removePermissionByRole(@Param("id") String id);

	public int insertByBatch(@Param("roleId") String roleId, @Param("permissionIds") String[] permissionIds);

}
package com.paladin.common.mapper.syst;

import org.apache.ibatis.annotations.Param;

import com.paladin.common.model.syst.SysUser;

public interface SysUserMapper extends tk.mybatis.mapper.common.base.select.SelectAllMapper<SysUser>, tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper<SysUser>, tk.mybatis.mapper.common.base.insert.InsertMapper<SysUser>, tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper<SysUser>, tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper<SysUser>, tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper<SysUser>, tk.mybatis.mapper.common.example.DeleteByExampleMapper<SysUser>, tk.mybatis.mapper.common.example.SelectOneByExampleMapper<SysUser>, tk.mybatis.mapper.common.example.SelectByExampleMapper<SysUser>, tk.mybatis.mapper.common.example.SelectCountByExampleMapper<SysUser>, tk.mybatis.mapper.common.example.UpdateByExampleMapper<SysUser>, tk.mybatis.mapper.common.example.UpdateByExampleSelectiveMapper<SysUser>, tk.mybatis.mapper.common.Marker {

	public int updateAccount(@Param("userId") String userId, @Param("originAccount") String originAccount, @Param("nowAccount") String nowAccount);

}

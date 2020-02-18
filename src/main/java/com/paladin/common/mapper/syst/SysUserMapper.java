package com.paladin.common.mapper.syst;

import org.apache.ibatis.annotations.Param;

import com.paladin.common.model.syst.SysUser;
import com.paladin.framework.core.configuration.mybatis.CustomMapper;

public interface SysUserMapper extends CustomMapper<SysUser> {

	public int updateAccount(@Param("userId") String userId, @Param("originAccount") String originAccount, @Param("nowAccount") String nowAccount);

}

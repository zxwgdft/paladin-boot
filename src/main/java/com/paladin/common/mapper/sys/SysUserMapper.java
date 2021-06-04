package com.paladin.common.mapper.sys;

import com.paladin.common.model.sys.SysUser;
import com.paladin.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface SysUserMapper extends CommonMapper<SysUser> {

    @Update("UPDATE sys_user SET account = #{nowAccount}, update_time=now() WHERE user_id = #{userId}")
    int updateAccount(@Param("userId") String userId, @Param("nowAccount") String nowAccount);

    @Update("UPDATE sys_user SET last_login_time=now() WHERE user_id = #{userId}")
    int updateLastLoginTime(String account);
}

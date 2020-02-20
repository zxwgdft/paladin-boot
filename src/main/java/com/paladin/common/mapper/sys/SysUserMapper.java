package com.paladin.common.mapper.sys;

import com.paladin.common.model.sys.SysUser;
import com.paladin.framework.mybatis.CustomMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface SysUserMapper extends CustomMapper<SysUser> {

    @Update("UPDATE sys_user SET account = #{nowAccount}, update_time=now() WHERE user_id = #{userId} AND account = #{originAccount}")
    int updateAccount(@Param("userId") String userId, @Param("originAccount") String originAccount, @Param("nowAccount") String nowAccount);

}

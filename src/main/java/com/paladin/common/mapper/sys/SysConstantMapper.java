package com.paladin.common.mapper.sys;

import com.paladin.common.model.sys.SysConstant;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysConstantMapper {

    @Select("SELECT `type`,`code`,`name`,order_no orderNo FROM sys_constant ORDER BY `type` ASC, order_no ASC")
    List<SysConstant> findList();
}
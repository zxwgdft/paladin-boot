package com.paladin.common.service.sys;

import com.paladin.common.mapper.sys.SysConstantMapper;
import com.paladin.common.model.sys.SysConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConstantService {

    @Autowired
    private SysConstantMapper constantMapper;

}
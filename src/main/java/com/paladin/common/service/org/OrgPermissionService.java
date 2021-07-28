package com.paladin.common.service.org;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paladin.common.mapper.org.OrgPermissionMapper;
import com.paladin.common.model.org.OrgPermission;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrgPermissionService extends ServiceSupport<OrgPermission, OrgPermissionMapper> {

    public List<OrgPermission> findPermission4Grant() {
        return findList(new LambdaQueryWrapper<OrgPermission>().eq(OrgPermission::getGrantable, true));
    }

}
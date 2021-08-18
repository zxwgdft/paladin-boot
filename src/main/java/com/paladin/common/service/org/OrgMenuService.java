package com.paladin.common.service.org;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paladin.common.mapper.org.OrgMenuMapper;
import com.paladin.common.model.org.OrgMenu;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrgMenuService extends ServiceSupport<OrgMenu, OrgMenuMapper> {

    public List<OrgMenu> findMenu4Grant() {
        return findList(new LambdaQueryWrapper<OrgMenu>().eq(OrgMenu::getGrantable, true));
    }

}
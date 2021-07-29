package com.paladin.common.service.org;


import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.common.core.security.MenuContainer;
import com.paladin.common.mapper.org.OrgRoleMenuMapper;
import com.paladin.framework.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrgRoleMenuService {

    @Autowired
    private OrgRoleMenuMapper orgRoleMenuMapper;

    public List<Integer> getMenuByRole(int id) {
        return orgRoleMenuMapper.getMenuByRole(id);
    }

    @Transactional
    public void grantAuthorization(int roleId, int[] menuIds) {
        orgRoleMenuMapper.removeMenuByRole(roleId);
        if (menuIds != null && menuIds.length > 0) {
            MenuContainer menuContainer = DataCacheHelper.getData(MenuContainer.class);
            if (menuContainer == null) throw new BusinessException("授权异常");

            List<Integer> pids = new ArrayList<>(menuIds.length);
            for (int pid : menuIds) {
                if (menuContainer.getMenu(pid) != null) {
                    pids.add(pid);
                }
            }

            if (pids.size() > 0) {
                orgRoleMenuMapper.insertByBatch(roleId, pids);
            }
        }
        DataCacheHelper.reloadCache(MenuContainer.class);
    }

}
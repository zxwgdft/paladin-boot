package com.paladin.common.service.org;


import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.common.core.security.PermissionContainer;
import com.paladin.common.mapper.org.OrgRolePermissionMapper;
import com.paladin.framework.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrgRolePermissionService {

    @Autowired
    private OrgRolePermissionMapper orgRolePermissionMapper;

    public List<Integer> getPermissionByRole(int id) {
        return orgRolePermissionMapper.getPermissionByRole(id);
    }

    @Transactional
    public void grantAuthorization(int roleId, int[] permissionIds) {
        orgRolePermissionMapper.removePermissionByRole(roleId);
        if (permissionIds != null && permissionIds.length > 0) {
            PermissionContainer permissionContainer = DataCacheHelper.getData(PermissionContainer.class);
            if (permissionContainer == null) throw new BusinessException("授权异常");

            List<Integer> pids = new ArrayList<>(permissionIds.length);
            for (int pid : permissionIds) {
                if (permissionContainer.getPermission(pid) != null) {
                    pids.add(pid);
                }
            }

            if (pids.size() > 0) {
                orgRolePermissionMapper.insertByBatch(roleId, pids);
            }
        }

        DataCacheHelper.reloadCache(PermissionContainer.class);
    }


}
package com.paladin.common.service.org;


import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.common.core.security.Permission;
import com.paladin.common.core.security.PermissionContainer;
import com.paladin.common.mapper.org.OrgRolePermissionMapper;
import com.paladin.framework.cache.DataCacheManager;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrgRolePermissionService {

    @Autowired
    private OrgRolePermissionMapper orgRolePermissionMapper;

    public List<String> getPermissionByRole(String id) {
        return orgRolePermissionMapper.getPermissionByRole(id);
    }

    @Transactional
    public boolean grantAuthorization(String roleId, String[] permissionIds) {
        if (StringUtil.isEmpty(roleId)) {
            return false;
        }

        orgRolePermissionMapper.removePermissionByRole(roleId);

        if (permissionIds != null && permissionIds.length > 0) {

            PermissionContainer permissionContainer = DataCacheHelper.getData(PermissionContainer.class);
            if (permissionContainer == null) throw new BusinessException("授权异常");

            List<String> pids = new ArrayList<>(permissionIds.length);

            HashMap<String, Permission> permissionMap = new HashMap<>();
            for (String pid : permissionIds) {
                if (permissionContainer.getPermission(pid) != null) {
                    pids.add(pid);
                }
            }

            if (pids.size() > 0) {
                orgRolePermissionMapper.insertByBatch(roleId, pids);
            }
        }


        DataCacheHelper.reloadCache(PermissionContainer.class);
        return true;
    }


}
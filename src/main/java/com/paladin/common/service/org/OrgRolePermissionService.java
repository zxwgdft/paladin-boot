package com.paladin.common.service.org;

import com.paladin.common.core.permission.Permission;
import com.paladin.common.core.permission.PermissionContainer;
import com.paladin.common.core.permission.RoleContainer;
import com.paladin.common.mapper.org.OrgRolePermissionMapper;
import com.paladin.common.model.org.OrgRolePermission;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrgRolePermissionService extends ServiceSupport<OrgRolePermission> {

    @Autowired
    private OrgRolePermissionMapper orgRolePermissionMapper;

    public List<String> getPermissionByRole(String id) {
        return orgRolePermissionMapper.getPermissionByRole(id);
    }

    @Transactional
    public boolean grantAuthorization(String roleId, String[] permissionIds) {
        if (roleId == null || roleId.length() == 0) {
            return false;
        }

        orgRolePermissionMapper.removePermissionByRole(roleId);
        if (permissionIds != null && permissionIds.length > 0) {
            List<String> pids = new ArrayList<>(permissionIds.length);

            HashMap<String, Permission> permissionMap = new HashMap<>();
            for (String pid : permissionIds) {
                Permission permission = PermissionContainer.getPermission(pid);
                if (permission != null) {
                    permissionMap.put(permission.getId(), permission);
                }
            }

            for (Permission permission : permissionMap.values()) {
                if (hasPermission(permission, permissionMap)) {
                    pids.add(permission.getId());
                }
            }

            if (pids.size() > 0) {
                orgRolePermissionMapper.insertByBatch(roleId, pids.toArray(new String[pids.size()]));
            }
        }

        RoleContainer.updateData();
        return true;
    }

    /**
     * 如果权限子级存在未授权的子权限，则该权限不应该被授权（基于授权该权限，则其下所有子权限也被授予规则）
     *
     * @param permission
     * @param permissionMap
     * @return
     */
    private boolean hasPermission(Permission permission, Map<String, Permission> permissionMap) {
        if (!permission.isGrantable()) {
            return false;
        }

        if (!permission.isLeaf()) {
            for (Permission child : permission.getChildren()) {
                if (!child.isGrantable()) {
                    continue;
                }

                if (!permissionMap.containsKey(child.getId())) {
                    return false;
                }

                if (!hasPermission(child, permissionMap)) {
                    return false;
                }
            }
        }
        return true;
    }

}
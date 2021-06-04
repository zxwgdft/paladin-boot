package com.paladin.common.core.security;

import com.paladin.common.mapper.org.OrgPermissionMapper;
import com.paladin.common.mapper.org.OrgRolePermissionMapper;
import com.paladin.common.model.org.OrgRolePermission;
import com.paladin.framework.cache.DataCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class PermissionDataCache implements DataCache<PermissionContainer> {


    @Autowired
    private OrgPermissionMapper orgPermissionMapper;

    @Autowired
    private OrgRolePermissionMapper orgRolePermissionMapper;


    public String getId() {
        return "PERMISSION_CACHE";
    }

    @Override
    public PermissionContainer loadData(long version) {

        /**
         * 如果权限只是一个code识别，可以用code作为主键，减少了读取复杂度。
         * 以下方式封装了Permission，如果存在权限属性的扩展，则需要按这种方式去加载，
         * 并一并把有用的数据缓存起来。
         * */
        List<Permission> permissions = orgPermissionMapper.findPermission();

        int size = (int) (permissions.size() / 0.75 + 1);

        Map<String, Permission> permissionMap = new HashMap<>(size);
        for (Permission permission : permissions) {
            permissionMap.put(permission.getId(), permission);
        }

        List<OrgRolePermission> rolePermissions = orgRolePermissionMapper.findList();

        Map<String, Set<String>> role2CodesMap = new HashMap<>();

        for (OrgRolePermission rolePermission : rolePermissions) {

            String roleId = rolePermission.getRoleId();
            String permissionId = rolePermission.getPermissionId();

            Permission permission = permissionMap.get(permissionId);
            if (permission == null) continue;

            Set<String> codeSet = role2CodesMap.get(roleId);
            if (codeSet == null) {
                codeSet = new HashSet<>();
                role2CodesMap.put(roleId, codeSet);
            }

            codeSet.add(permission.getCode());
        }

        return new PermissionContainer(permissionMap, role2CodesMap);
    }
}

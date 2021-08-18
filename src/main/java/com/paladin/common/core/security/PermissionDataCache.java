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
        List<CodePermission> codePermissions = orgPermissionMapper.findPermission();

        int size = (int) (codePermissions.size() / 0.75 + 1);

        Map<Integer, CodePermission> permissionMap = new HashMap<>(size);
        Map<String, CodePermission> code2permissionMap = new HashMap<>(size);

        for (CodePermission codePermission : codePermissions) {
            permissionMap.put(codePermission.getId(), codePermission);
            code2permissionMap.put(codePermission.getCode(), codePermission);
        }

        List<OrgRolePermission> rolePermissions = orgRolePermissionMapper.findList();

        Map<Integer, Set<String>> role2CodesMap = new HashMap<>();
        Map<Integer, Set<CodePermission>> role2PermissionsMap = new HashMap<>();


        for (OrgRolePermission rolePermission : rolePermissions) {

            Integer roleId = rolePermission.getRoleId();
            Integer permissionId = rolePermission.getPermissionId();

            CodePermission codePermission = permissionMap.get(permissionId);
            if (codePermission == null) continue;

            Set<String> codeSet = role2CodesMap.get(roleId);
            if (codeSet == null) {
                codeSet = new HashSet<>();
                role2CodesMap.put(roleId, codeSet);
            }

            codeSet.add(codePermission.getCode());


            Set<CodePermission> codePermissionSet = role2PermissionsMap.get(roleId);
            if (codePermissionSet == null) {
                codePermissionSet = new HashSet<>();
                role2PermissionsMap.put(roleId, codePermissionSet);
            }

            codePermissionSet.add(codePermission);
        }

        return new PermissionContainer(permissionMap, code2permissionMap, role2CodesMap, role2PermissionsMap);
    }
}

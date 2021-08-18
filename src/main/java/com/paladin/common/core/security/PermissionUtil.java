package com.paladin.common.core.security;


import com.paladin.common.core.CommonUserSession;
import com.paladin.common.core.cache.DataCacheHelper;

public class PermissionUtil {

    public static boolean hasPermission(String permissionCode) {
        return hasPermission(CommonUserSession.getCurrentUserSession(), permissionCode);
    }

    public static boolean hasPermission(CommonUserSession userSession, String permissionCode) {
        PermissionContainer permissionContainer = DataCacheHelper.getData(PermissionContainer.class);
        if (permissionContainer != null) {
            if (userSession.isSystemAdmin()) {
                CodePermission codePermission = permissionContainer.getPermission(permissionCode);
                return codePermission != null && codePermission.isAdmin();
            } else {
                for (int roleId : userSession.getRoleIdList()) {
                    if (permissionContainer.hasPermission(roleId, permissionCode)) return true;
                }
            }
        }
        return false;
    }
}

package com.paladin.common.core.security;


import com.paladin.common.core.CommonUserSession;
import com.paladin.common.core.cache.DataCacheHelper;
import org.springframework.stereotype.Component;

/**
 * 权限工具类（包括数据权限处理、功能权限判断、权限获取）
 */
@Component
public class PermissionUtil {

    public static DataPermissionParam getUserDataPermission() {
        CommonUserSession userSession = CommonUserSession.getCurrentUserSession();
        DataPermissionParam param = new DataPermissionParam();
        // TODO
        return param;
    }

    public static boolean hasPermission(String permissionCode) {
        return hasPermission(CommonUserSession.getCurrentUserSession(), permissionCode);
    }

    public static boolean hasPermission(CommonUserSession userSession, String permissionCode) {
        PermissionContainer permissionContainer = DataCacheHelper.getData(PermissionContainer.class);
        if (permissionContainer != null) {
            if (userSession.isSystemAdmin()) {
                Permission permission = permissionContainer.getPermission(permissionCode);
                return permission != null && permission.isAdmin();
            } else {
                for (String roleId : userSession.getRoles()) {
                    if (permissionContainer.hasPermission(roleId, permissionCode)) return true;
                }
            }
        }
        return false;
    }
}

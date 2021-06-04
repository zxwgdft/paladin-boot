package com.paladin.common.core.security;


import com.paladin.common.core.CommonUserSession;
import com.paladin.framework.cache.DataCacheManager;
import org.springframework.stereotype.Component;

/**
 * 权限工具类（包括数据权限处理、功能权限判断、权限获取）
 */
@Component
public class PermissionUtil {

    private static DataCacheManager dataCacheManager;

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
        return false;
    }
}

package com.paladin.demo.core;

import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.demo.service.org.OrgAgencyContainer;

import java.util.List;

public class DataPermissionUtil {

    public static DataPermissionParam getDataPermissionParam() {
        // 增加数据权限过滤
        // 应用管理员级别可以查看所有人员数据
        // 机构管理员可以查看所管辖机构及以下单位人员数据
        // 个人只能查看自己的数据
        DemoUserSession userSession = DemoUserSession.getCurrentUserSession();
        int roleLevel = userSession.getRoleLevel();

        DataPermissionParam permissionParam = new DataPermissionParam();
        if (roleLevel >= DemoUserSession.ROLE_LEVEL_APP_ADMIN) {
            permissionParam.setHasPermission(true);
            permissionParam.setHasAll(true);
        } else {
            if (roleLevel >= DemoUserSession.ROLE_LEVEL_AGENCY_ADMIN) {
                Integer agencyId = userSession.getAgencyId();
                if (agencyId != null) {
                    OrgAgencyContainer.Agency agency = DataCacheHelper.getData(OrgAgencyContainer.class).getAgency(agencyId);
                    if (agency != null) {
                        List<Integer> ids = agency.getSelfAndChildrenIds();
                        if (ids.size() == 1) {
                            permissionParam.setHasPermission(true);
                            permissionParam.setAgencyId(ids.get(0));
                        } else {
                            permissionParam.setHasPermission(true);
                            permissionParam.setAgencyIds(ids);
                        }
                    }
                }
            }

            if (!permissionParam.isHasPermission() && roleLevel >= DemoUserSession.ROLE_LEVEL_PERSONNEL) {
                permissionParam.setHasPermission(true);
                permissionParam.setUserId(userSession.getUserId());
            }
        }

        return permissionParam;
    }

}

package com.paladin.common.service.org;

import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.common.core.security.RoleContainer;
import com.paladin.common.mapper.org.OrgRoleMapper;
import com.paladin.common.model.org.OrgRole;
import com.paladin.common.service.org.dto.OrgRoleDTO;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgRoleService extends ServiceSupport<OrgRole, OrgRoleMapper> {

    @Transactional
    public void saveRole(OrgRoleDTO orgRoleDTO) {
        OrgRole model = new OrgRole();
        SimpleBeanCopyUtil.simpleCopy(orgRoleDTO, model);
        save(model);
        // 更新缓存
        DataCacheHelper.reloadCache(RoleContainer.class);
    }

    @Transactional
    public void updateRole(OrgRoleDTO orgRoleDTO) {
        Integer id = orgRoleDTO.getId();
        if (id == null) {
            throw new BusinessException("找不到更新角色");
        }

        OrgRole model = getWhole(id);
        if (model == null) {
            throw new BusinessException("找不到更新角色");
        }

        SimpleBeanCopyUtil.simpleCopy(orgRoleDTO, model);
        updateWhole(model);

        // 更新缓存
        DataCacheHelper.reloadCache(RoleContainer.class);
    }

    public void updateRoleEnabled(int id) {
        OrgRole role = new OrgRole();
        role.setId(id);
        role.setEnable(true);
        if (!updateSelection(role)) {
            throw new BusinessException("启用角色失败！");
        }
    }

    public void updateRoleDisabled(int id) {
        OrgRole role = new OrgRole();
        role.setId(id);
        role.setEnable(false);
        if (!updateSelection(role)) {
            throw new BusinessException("停用角色失败！");
        }
    }
}
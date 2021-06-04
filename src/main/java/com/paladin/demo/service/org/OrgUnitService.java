package com.paladin.demo.service.org;

import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.demo.mapper.org.OrgUnitMapper;
import com.paladin.demo.model.org.OrgUnit;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgUnitService extends ServiceSupport<OrgUnit, OrgUnitMapper> {

    @Transactional
    public boolean saveUnit(OrgUnit model) {
        save(model);
        DataCacheHelper.reloadCache(OrgUnitContainer.class);
        return true;
    }

    @Transactional
    public boolean updateUnit(OrgUnit model) {
        updateWhole(model);
        DataCacheHelper.reloadCache(OrgUnitContainer.class);
        return true;
    }

    @Transactional
    public boolean removeUnit(String id) {
        OrgUnitContainer unitContainer = DataCacheHelper.getData(OrgUnitContainer.class);
        OrgUnitContainer.Unit unit = unitContainer.getUnit(id);
        if (unit == null) {
            throw new BusinessException("找不到对应删除的单位");
        }
        if (unit.getChildren().size() > 0) {
            throw new BusinessException("请先删除该单位下所有子单位");
        }
        deleteById(id);
        DataCacheHelper.reloadCache(OrgUnitContainer.class);
        return true;
    }
}
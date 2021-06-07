package com.paladin.demo.service.org;

import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.demo.mapper.org.OrgUnitMapper;
import com.paladin.demo.model.org.OrgUnit;
import com.paladin.demo.service.org.dto.OrgUnitDTO;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgUnitService extends ServiceSupport<OrgUnit, OrgUnitMapper> {

    @Transactional
    public void saveUnit(OrgUnitDTO orgUnitDTO) {
        save(SimpleBeanCopyUtil.simpleCopy(orgUnitDTO, new OrgUnit()));
        DataCacheHelper.reloadCache(OrgUnitContainer.class);
    }

    @Transactional
    public void updateUnit(OrgUnitDTO orgUnitDTO) {
        updateWhole(SimpleBeanCopyUtil.simpleCopy(orgUnitDTO, getWhole(orgUnitDTO.getId())));
        DataCacheHelper.reloadCache(OrgUnitContainer.class);
    }

    @Transactional
    public void removeUnit(String id) {
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
    }
}
package com.paladin.demo.service.org;

import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.demo.mapper.org.OrgAgencyMapper;
import com.paladin.demo.model.org.OrgAgency;
import com.paladin.demo.service.org.dto.OrgAgencyDTO;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgAgencyService extends ServiceSupport<OrgAgency, OrgAgencyMapper> {

    @Transactional
    public void saveAgency(OrgAgencyDTO orgAgencyDTO) {
        save(SimpleBeanCopyUtil.simpleCopy(orgAgencyDTO, new OrgAgency()));
        DataCacheHelper.reloadCache(OrgAgencyContainer.class);
    }

    @Transactional
    public void updateAgency(OrgAgencyDTO orgAgencyDTO) {
        updateWhole(SimpleBeanCopyUtil.simpleCopy(orgAgencyDTO, getWhole(orgAgencyDTO.getId())));
        DataCacheHelper.reloadCache(OrgAgencyContainer.class);
    }

    @Transactional
    public void removeAgency(int id) {
        OrgAgencyContainer agencyContainer = DataCacheHelper.getData(OrgAgencyContainer.class);
        OrgAgencyContainer.Agency agency = agencyContainer.getAgency(id);
        if (agency == null) {
            throw new BusinessException("找不到对应删除的单位");
        }
        if (agency.getChildren().size() > 0) {
            throw new BusinessException("请先删除该单位下所有子单位");
        }
        if (deleteById(id)) {
            DataCacheHelper.reloadCache(OrgAgencyContainer.class);
        }
    }
}
package com.paladin.demo.service.org;

import com.paladin.demo.mapper.org.OrgAgencyMapper;
import com.paladin.demo.model.org.OrgAgency;
import com.paladin.framework.cache.DataCache;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单位容器，缓存了所有单位信息，并进行了一些封装
 *
 * @author TontoZhou
 * @since 2020/3/9
 */
@Slf4j
@Component
public class OrgAgencyDataCache implements DataCache<OrgAgencyContainer> {

    @Autowired
    private OrgAgencyMapper orgAgencyMapper;

    public String getId() {
        return "ORG_AGENCY_CACHE";
    }

    @Override
    public OrgAgencyContainer loadData(long version) {
        List<OrgAgency> orgAgencies = orgAgencyMapper.selectList(null);

        Map<Integer, OrgAgencyContainer.Agency> agencyMap = new HashMap<>();
        List<OrgAgencyContainer.Agency> rootList = new ArrayList<>();

        if (orgAgencies != null && orgAgencies.size() > 0) {
            for (OrgAgency orgagency : orgAgencies) {
                OrgAgencyContainer.Agency agency = SimpleBeanCopyUtil.simpleCopy(orgagency, OrgAgencyContainer.Agency.class);
                agency.setChildren(new ArrayList<>());
                agencyMap.put(agency.getId(), agency);
            }

            for (OrgAgency orgAgency : orgAgencies) {
                Integer id = orgAgency.getId();
                Integer parentId = orgAgency.getParentId();

                OrgAgencyContainer.Agency agency = agencyMap.get(id);
                OrgAgencyContainer.Agency parent = agencyMap.get(parentId);
                if (parent == null) {
                    rootList.add(agency);
                } else {
                    agency.setParent(parent);
                    parent.getChildren().add(agency);
                }
            }
        }

        for (OrgAgencyContainer.Agency agency : rootList) {
            initSelfAndChildrenIds(agency);
        }

        return new OrgAgencyContainer(agencyMap, rootList);
    }

    private List<Integer> initSelfAndChildrenIds(OrgAgencyContainer.Agency agency) {
        List<Integer> ids = new ArrayList<>(1 + agency.getChildren().size());
        ids.add(agency.getId());
        for (OrgAgencyContainer.Agency child : agency.getChildren()) {
            ids.addAll(initSelfAndChildrenIds(child));
        }
        agency.setSelfAndChildrenIds(ids);
        return ids;
    }

}

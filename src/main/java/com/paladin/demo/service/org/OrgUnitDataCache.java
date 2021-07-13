package com.paladin.demo.service.org;

import com.paladin.demo.mapper.org.OrgUnitMapper;
import com.paladin.demo.model.org.OrgUnit;
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
public class OrgUnitDataCache implements DataCache<OrgUnitContainer> {

    @Autowired
    private OrgUnitMapper orgUnitMapper;

    public String getId() {
        return "ORG_UNIT_CACHE";
    }

    @Override
    public OrgUnitContainer loadData(long version) {
        List<OrgUnit> orgUnits = orgUnitMapper.selectList(null);

        Map<String, OrgUnitContainer.Unit> unitMap = new HashMap<>();
        List<OrgUnitContainer.Unit> rootList = new ArrayList<>();

        if (orgUnits != null && orgUnits.size() > 0) {
            for (OrgUnit orgUnit : orgUnits) {
                OrgUnitContainer.Unit unit = SimpleBeanCopyUtil.simpleCopy(orgUnit, OrgUnitContainer.Unit.class);
                unit.setChildren(new ArrayList<>());
                unitMap.put(unit.getId(), unit);
            }

            for (OrgUnit orgUnit : orgUnits) {
                String id = orgUnit.getId();
                String parentId = orgUnit.getParentId();

                OrgUnitContainer.Unit unit = unitMap.get(id);
                OrgUnitContainer.Unit parent = unitMap.get(parentId);
                if (parent == null) {
                    rootList.add(unit);
                } else {
                    unit.setParent(parent);
                    parent.getChildren().add(unit);
                }
            }
        }

        for (OrgUnitContainer.Unit unit : rootList) {
            initSelfAndChildrenIds(unit);
        }

        return new OrgUnitContainer(unitMap, rootList);
    }

    private List<String> initSelfAndChildrenIds(OrgUnitContainer.Unit unit) {
        List<String> ids = new ArrayList<>(1 + unit.getChildren().size());
        ids.add(unit.getId());
        for (OrgUnitContainer.Unit child : unit.getChildren()) {
            ids.addAll(initSelfAndChildrenIds(child));
        }
        unit.setSelfAndChildrenIds(ids);
        return ids;
    }

}

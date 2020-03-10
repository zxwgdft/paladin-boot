package com.paladin.demo.service.org;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paladin.demo.model.org.OrgUnit;
import com.paladin.framework.service.VersionContainer;
import com.paladin.framework.service.VersionContainerManager;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 单位容器，缓存了所有单位信息，并进行了一些封装
 *
 * @author TontoZhou
 * @since 2020/3/9
 */
@Slf4j
@Component
public class OrgUnitContainer implements VersionContainer {

    @Autowired
    private OrgUnitService orgUnitService;

    private static Map<String, Unit> unitMap;
    private static List<Unit> rootList;

    public void initialize() {

        List<OrgUnit> orgUnits = orgUnitService.findAll();

        Map<String, Unit> unitMap = new HashMap<>();
        List<Unit> rootList = new ArrayList<>();

        if (orgUnits != null && orgUnits.size() > 0) {

            for (OrgUnit orgUnit : orgUnits) {
                Unit unit = SimpleBeanCopyUtil.simpleCopy(orgUnit, Unit.class);
                unit.setChildren(new ArrayList<>());
                unitMap.put(unit.getId(), unit);
            }

            for (OrgUnit orgUnit : orgUnits) {
                String id = orgUnit.getId();
                String parentId = orgUnit.getParentId();

                Unit unit = unitMap.get(id);
                Unit parent = unitMap.get(parentId);
                if (parent == null) {
                    rootList.add(unit);
                } else {
                    unit.setParent(parent);
                    parent.getChildren().add(unit);
                }
            }
        }

        OrgUnitContainer.unitMap = Collections.unmodifiableMap(unitMap);
        OrgUnitContainer.rootList = Collections.unmodifiableList(rootList);
    }


    private final static String container_id = "org_unit_container";

    @Override
    public String getId() {
        return container_id;
    }

    @Override
    public boolean versionChangedHandle(long version) {
        initialize();
        return true;
    }

    // 更新数据，重新初始化数据
    public static void updateData() {
        VersionContainerManager.versionChanged(container_id);
    }

    /**
     * 获取单位
     *
     * @param id
     * @return
     */
    public static Unit getUnit(String id) {
        return unitMap.get(id);
    }

    /**
     * 获取单位名字
     *
     * @param id
     * @return
     */
    public static String getUnitName(String id) {
        Unit unit = unitMap.get(id);
        return unit == null ? "" : unit.getName();
    }

    /**
     * 获取单位树
     *
     * @return
     */
    public static List<Unit> getUnitTree() {
        return rootList;
    }


    @Getter
    @Setter
    public static class Unit {

        private String id;

        // 单位名称
        private String name;

        // 单位类型
        private Integer type;

        // 联系人
        private String contact;

        // 联系电话
        private String contactPhone;

        // 上级单位
        private String parentId;

        // 上级单位
        @JsonIgnore
        private Unit parent;

        // 下级
        private List<Unit> children;

    }

}

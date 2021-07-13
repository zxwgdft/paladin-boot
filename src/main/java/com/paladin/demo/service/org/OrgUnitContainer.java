package com.paladin.demo.service.org;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 单位容器
 *
 * @author TontoZhou
 * @since 2020/3/9
 */
@Slf4j
public class OrgUnitContainer {

    private Map<String, Unit> unitMap;
    private List<Unit> rootList;

    public OrgUnitContainer(Map<String, Unit> unitMap, List<Unit> rootList) {
        this.unitMap = unitMap;
        this.rootList = rootList;
    }

    /**
     * 获取单位
     *
     * @param id
     * @return
     */
    public Unit getUnit(String id) {
        return unitMap.get(id);
    }

    /**
     * 获取单位名字
     *
     * @param id
     * @return
     */
    public String getUnitName(String id) {
        Unit unit = unitMap.get(id);
        return unit == null ? "" : unit.getName();
    }

    /**
     * 获取单位树
     *
     * @return
     */
    public List<Unit> getUnitTree() {
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

        @JsonIgnore
        public List<String> selfAndChildrenIds;

    }

}

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
public class OrgAgencyContainer {

    private Map<Integer, Agency> agencyMap;
    private List<Agency> rootList;

    public OrgAgencyContainer(Map<Integer, Agency> agencyMap, List<Agency> rootList) {
        this.agencyMap = agencyMap;
        this.rootList = rootList;
    }

    /**
     * 获取单位
     *
     * @param id
     * @return
     */
    public Agency getAgency(Integer id) {
        return agencyMap.get(id);
    }

    /**
     * 获取单位名字
     *
     * @param id
     * @return
     */
    public String getAgencyName(Integer id) {
        Agency agency = agencyMap.get(id);
        return agency == null ? "" : agency.getName();
    }

    /**
     * 获取单位树
     *
     * @return
     */
    public List<Agency> getAgencyTree() {
        return rootList;
    }


    @Getter
    @Setter
    public static class Agency {

        private Integer id;

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
        private Agency parent;

        // 下级
        private List<Agency> children;

        @JsonIgnore
        public List<Integer> selfAndChildrenIds;

        @JsonIgnore
        public String getFullName() {
            return _getFullName(null);
        }

        private String _getFullName(String prefix) {
            String fullName = prefix == null ? name : (name + "-" + prefix);
            return parent == null ? fullName : parent._getFullName(fullName);
        }
    }

}

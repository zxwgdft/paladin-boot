package com.paladin.demo.core;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/3/17
 */
@Getter
@Setter
public class DataPermissionParam {

    // 多机构权限（机构权限条件互斥）
    private List<Integer> agencyIds;
    // 单一机构权限
    private Integer agencyId;

    // 某用户数据权限
    private String userId;

    // 是否有所有数据权限
    private boolean hasAll;
    // 是否有权限
    private boolean hasPermission;

}

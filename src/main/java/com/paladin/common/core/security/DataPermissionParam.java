package com.paladin.common.core.security;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/5/12
 */
@Getter
@Setter
public class DataPermissionParam {

    // 机构ID
    private Integer unitId;

    // 是否有权限
    private boolean hasPermission = true;

}

package com.paladin.common.core.security;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2021/3/29
 */
@Getter
@Setter
public class Role {

    private Integer id;
    private String name;
    private Integer level;
    private Boolean enabled;

}

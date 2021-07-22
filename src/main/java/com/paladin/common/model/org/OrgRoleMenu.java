package com.paladin.common.model.org;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * @author TontoZhou
 * @since 2021/3/29
 */
@Getter
@Setter
public class OrgRoleMenu {

    @Id
    private Integer roleId;
    @Id
    private Integer menuId;

}

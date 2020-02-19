package com.paladin.common.model.org;

import com.paladin.framework.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
public class OrgRole extends BaseModel {

    public static final String COLUMN_FIELD_IS_DEFAULT = "isDefault";
    public static final String COLUMN_FIELD_ROLE_LEVEL = "roleLevel";

    @Id
    private String id;

    // 角色名称
    private String roleName;

    // 角色等级
    private Integer roleLevel;

    // 角色说明
    private String roleDesc;

    // 是否默认角色（1是0否）
    private Integer isDefault;

    // 是否启用 1是0否
    private Integer enable;


}
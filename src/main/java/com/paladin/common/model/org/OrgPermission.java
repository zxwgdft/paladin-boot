package com.paladin.common.model.org;

import com.paladin.framework.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
public class OrgPermission extends BaseModel {

    public static final String FIELD_GRANTABLE = "grantable";

    @Id
    private String id;

    // 权限名称
    private String name;

    private String url;

    private String code;

    // 是否菜单
    private Integer isMenu;

    // 图标
    private String menuIcon;

    // 权限描述
    private String description;

    // 父ID
    private String parentId;

    // 列表顺序
    private Integer listOrder;

    // 是否系统管理员权限
    private Integer isAdmin;

    // 是否可授权
    private Integer grantable;


}
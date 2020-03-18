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

    // 权限对应URL
    private String url;

    // 权限对应code
    private String code;

    // 是否菜单
    private Integer isMenu;

    // 是否页面
    private Integer isPage;

    // 图标
    private String menuIcon;

    // 权限描述
    private String description;

    // 父ID
    private String parentId;

    // 列表顺序
    private Integer listOrder;

    // 是否系统管理员权限，正常情况系统管理员应该不拥有业务相关功能权限，该部分权限应该由应用管理员通过授权赋权
    private Integer isAdmin;

    // 是否可授权
    private Integer grantable;


}
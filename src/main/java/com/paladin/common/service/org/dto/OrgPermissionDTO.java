package com.paladin.common.service.org.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrgPermissionDTO {

    // id
    private String id;

    // 权限名称
    private String name;

    // 权限类型1：菜单2：功能3:分类
    private Integer type;

    // 表现形式1：URL2：CODE
    private Integer expressionType;

    // 表现内容
    private String expressionContent;

    // 权限描述
    private String description;

    // 父ID
    private String parentId;

    // 列表顺序
    private Integer listOrder;

    // 是否系统管理员权限
    private Integer isAdminMenu;


}
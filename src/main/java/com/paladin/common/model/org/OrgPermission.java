package com.paladin.common.model.org;

import com.paladin.framework.common.UnDeleteBaseModel;
import javax.persistence.Id;

public class OrgPermission extends UnDeleteBaseModel {

	public static final String COLUMN_FIELD_GRANTABLE = "grantable";
	
	// id
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(Integer isMenu) {
		this.isMenu = isMenu;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getListOrder() {
		return listOrder;
	}

	public void setListOrder(Integer listOrder) {
		this.listOrder = listOrder;
	}

	public Integer getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Integer getGrantable() {
		return grantable;
	}

	public void setGrantable(Integer grantable) {
		this.grantable = grantable;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	

}
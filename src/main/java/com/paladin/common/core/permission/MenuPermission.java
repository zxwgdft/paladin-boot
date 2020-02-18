package com.paladin.common.core.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import com.paladin.common.model.org.OrgPermission;
import com.paladin.framework.common.BaseModel;

public class MenuPermission {

	private OrgPermission source;

	// 是否拥有
	private boolean owned;
	// 是否菜单
	private boolean isMenu;

	private int listOrder;

	private Collection<MenuPermission> children;

	public MenuPermission(OrgPermission orgPermission, boolean owned) {
		this.source = orgPermission;
		this.isMenu = orgPermission.getIsMenu() == BaseModel.BOOLEAN_YES;
		this.owned = owned;
		Integer listOrder = orgPermission.getListOrder();
		this.listOrder = listOrder == null ? 0 : listOrder.intValue();
		this.children = new HashSet<>();
	}

	public void init() {
		if (children.size() > 0) {
			ArrayList<MenuPermission> list = new ArrayList<>(children);
			Collections.sort(list, new Comparator<MenuPermission>() {
				@Override
				public int compare(MenuPermission mr1, MenuPermission mr2) {
					return mr1.listOrder - mr2.listOrder;
				}
			});
			
			for(MenuPermission mp : children) {
				mp.init();
			}
			
			children = list;
		}
	}

	public void addChild(MenuPermission child) {
		children.add(child);
	}

	public String getId() {
		return source.getId();
	}

	public boolean isOwned() {
		return owned;
	}

	public void setOwned(boolean owned) {
		this.owned = owned;
	}

	public OrgPermission getSource() {
		return source;
	}

	public boolean isMenu() {
		return isMenu;
	}

	public Collection<MenuPermission> getChildren() {
		return children;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof MenuPermission) {
			MenuPermission mp = (MenuPermission) obj;
			return getId().equals(mp.getId());
		}
		return false;
	}

	public int getListOrder() {
		return listOrder;
	}

}

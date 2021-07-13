package com.paladin.common.mapper.org;

import com.paladin.common.core.security.Menu;
import com.paladin.common.model.org.OrgMenu;
import com.paladin.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrgMenuMapper extends CommonMapper<OrgMenu> {

    @Select("SELECT id, `name`, url, icon, parent_id AS parentId FROM org_menu ORDER BY order_no ASC")
    List<Menu> findMenu();
}
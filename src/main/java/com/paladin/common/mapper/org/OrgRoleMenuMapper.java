package com.paladin.common.mapper.org;

import com.paladin.common.model.org.OrgRoleMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2021/3/25
 */
public interface OrgRoleMenuMapper {
    @Select("SELECT role_id AS roleId, menu_id AS menuId FROM org_role_menu")
    List<OrgRoleMenu> findList();

    List<Integer> getMenuByRole(@Param("id") int id);

    int removeMenuByRole(@Param("id") int id);

    int insertByBatch(@Param("roleId") int roleId, @Param("menuIds") List<Integer> menuIds);
}

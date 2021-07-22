package com.paladin.common.core.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authz.Permission;

import java.io.Serializable;

/**
 * @author TontoZhou
 * @since 2020/3/17
 */
@Getter
@Setter
public class CodePermission implements Permission, Serializable {

    @ApiModelProperty("权限ID")
    private int id;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("权限code")
    private String code;

    @ApiModelProperty("系统管理员是否有权限")
    private boolean isAdmin;

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof CodePermission) {
            return id == ((CodePermission) obj).id;
        }
        return false;
    }

    public int hashCode() {
        return 17 * 31 + id;
    }

    @Override
    public boolean implies(Permission p) {
        if (!(p instanceof CodePermission)) {
            return false;
        }
        return ((CodePermission) p).id == id;
    }
}

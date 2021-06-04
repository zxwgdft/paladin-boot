package com.paladin.common.core.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.io.Serializable;

/**
 * @author TontoZhou
 * @since 2020/3/17
 */
@Getter
@Setter
public class Permission extends WildcardPermission implements Serializable {

    @ApiModelProperty("权限ID")
    private String id;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("权限code")
    private String code;


    public boolean equals(Object obj) {
        if (obj instanceof Permission) {
            return id.equals(((Permission) obj).id);
        }
        return false;
    }

    public int hashCode() {
        return 17 * 31 + id.hashCode();
    }

}

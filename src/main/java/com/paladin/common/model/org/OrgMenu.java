package com.paladin.common.model.org;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2021/3/25
 */
@Getter
@Setter
@ApiModel(description = "菜单权限")
public class OrgMenu {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("路径")
    private String url;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("父菜单ID")
    private Integer parentId;
    @ApiModelProperty("排序")
    private Integer orderNo;
    @ApiModelProperty("是否系统管理员拥有")
    private Boolean isAdmin;
    @ApiModelProperty("是否可授权")
    private Boolean grantable;


}

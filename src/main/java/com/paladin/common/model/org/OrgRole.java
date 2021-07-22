package com.paladin.common.model.org;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.paladin.framework.api.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "角色")
public class OrgRole extends BaseModel {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色等级")
    private Integer roleLevel;

    @ApiModelProperty("角色说明")
    private String roleDesc;

    @ApiModelProperty("是否启用")
    private Boolean enable;


}
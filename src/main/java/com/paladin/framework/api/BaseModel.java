package com.paladin.framework.api;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BaseModel implements Serializable {

    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(select = false)
    @ApiModelProperty(hidden = true)
    private String createBy;

    @TableField(select = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @TableField(select = false)
    @ApiModelProperty(hidden = true)
    private String updateBy;

}

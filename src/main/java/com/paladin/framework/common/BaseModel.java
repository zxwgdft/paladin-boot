package com.paladin.framework.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public abstract class BaseModel implements Serializable {

    public static final String COLUMN_FIELD_DELETED = "deleted";
    public static final String COLUMN_FIELD_CREATE_TIME = "createTime";

    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("创建者")
    private String createBy;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("更新者")
    private String updateBy;
    @ApiModelProperty("是否删除")
    private Boolean deleted;

}

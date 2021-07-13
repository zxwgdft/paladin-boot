package com.paladin.framework.api;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TontoZhou
 * @since 2021/3/16
 */
@Getter
@Setter
public class DeletedBaseModel extends BaseModel {

    @TableField(select = false)
    @ApiModelProperty(hidden = true)
    @TableLogic
    private Boolean deleted;
}

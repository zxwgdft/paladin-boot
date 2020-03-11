package com.paladin.framework.service;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuerySorts {

    @ApiModelProperty("排序字段")
    private String[] sort;
    @ApiModelProperty("排序方式")
    private String[] order;

}

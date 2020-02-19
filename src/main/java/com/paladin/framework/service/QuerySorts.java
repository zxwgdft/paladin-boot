package com.paladin.framework.service;

import io.swagger.annotations.ApiModelProperty;

public class QuerySorts {

    @ApiModelProperty("排序字段")
    private String[] sort;
    @ApiModelProperty("排序方式")
    private String[] order;

    public String[] getSort() {
        return sort;
    }

    public String[] getOrder() {
        return order;
    }

    public void setSort(String[] sort) {
        this.sort = sort;
    }

    public void setOrder(String[] order) {
        this.order = order;
    }

}

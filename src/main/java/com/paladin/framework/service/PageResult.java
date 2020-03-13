package com.paladin.framework.service;

import com.github.pagehelper.Page;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(description = "分页返回结果")
public class PageResult<T> {

    @ApiModelProperty("页码")
    private int page;

    @ApiModelProperty("每页大小")
    private int limit;

    @ApiModelProperty("总数据量")
    private long total;

    @ApiModelProperty("数据")
    private List<T> data;

    public PageResult() {

    }

    public PageResult(Page<T> page) {
        this.page = page.getPageNum();
        this.limit = page.getPageSize();
        this.total = page.getTotal();
        this.data = page;
    }

    public PageResult(Page page, List<T> data) {
        this.page = page.getPageNum();
        this.limit = page.getPageSize();
        this.total = page.getTotal();
        this.data = data;
    }

    public <E> PageResult<E> convert(Class<E> target) {
        PageResult<E> result = new PageResult<>();
        result.page = this.page;
        result.limit = this.limit;
        result.total = this.total;

        if (data != null) {
            result.data = SimpleBeanCopyUtil.simpleCopyList(data, target);
        }

        return result;
    }


}

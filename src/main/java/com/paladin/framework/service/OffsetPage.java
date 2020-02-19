package com.paladin.framework.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("分页请求")
public class OffsetPage extends QuerySort {

    public final static int DEFAULT_LIMIT = 15;
    public final static int MAX_LIMIT = 100;

    @ApiModelProperty("每页大小")
    private int limit = DEFAULT_LIMIT;
    @ApiModelProperty("分页起始序号")
    private int offset = -1;
    @ApiModelProperty("分页页码")
    private int page;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit > MAX_LIMIT) {
            limit = MAX_LIMIT;
        } else if (limit <= 0) {
            limit = DEFAULT_LIMIT;
        }

        this.limit = limit;
    }

    public int getOffset() {
        if (offset < 0) {
            offset = page > 0 ? ((page - 1) * limit) : 0;
        }

        return offset;
    }

    public void setOffset(int offset) {
        if (offset < 0) {
            offset = 0;
        }
        this.offset = offset;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page <= 0) {
            page = 1;
        }
        this.page = page;
    }

    /**
     * 强制手动设置分页大小（避开最大限制问题）
     *
     * @param limit
     */
    public void forceSetLimit(int limit) {
        this.limit = limit;
    }

}

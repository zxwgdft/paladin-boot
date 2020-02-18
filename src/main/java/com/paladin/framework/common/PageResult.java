package com.paladin.framework.common;

import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.paladin.framework.core.copy.SimpleBeanCopier.SimpleBeanCopyUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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

		PageHelper.clearPage();
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

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}

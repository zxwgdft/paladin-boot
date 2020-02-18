package com.paladin.framework.common;

/**
 * 对应只能逻辑删除的实体类
 * 
 * @author TontoZhou
 * @since 2018年1月22日
 */
public abstract class UnDeleteModel implements UnDelete {

	private Integer isDelete = 0;

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

}

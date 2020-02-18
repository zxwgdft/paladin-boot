package com.paladin.framework.common;

/**
 * 对应只能逻辑删除的实体类
 * 
 * @author TontoZhou
 * @since 2018年1月22日
 */
public interface UnDelete {

	public static final int STATUS_DELETE = 1;
	public static final int STATUS_UNDELETE = 0;
	
	public static final String COLUMN_FIELD_IS_DELETE = "isDelete";

	public Integer getIsDelete();

	public void setIsDelete(Integer isDelete);

}

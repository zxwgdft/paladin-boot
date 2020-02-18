package com.paladin.framework.core;

/**
 * 版本控制容器接口
 * @author TontoZhou
 * @since 2018年12月12日
 */
public interface VersionContainer {

	/**
	 * 容器ID
	 * @return
	 */
	public String getId();
	
	/**
	 * 执行顺序
	 * @return
	 */
	default public int order() {
		return 0;
	};
	
	/**
	 * 版本改变处理
	 * @param version
	 * @return
	 */
	public boolean versionChangedHandle(long version);
	
	
}

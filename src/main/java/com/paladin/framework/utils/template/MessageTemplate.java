package com.paladin.framework.utils.template;

/**
 * 消息模板接口
 * @author TontoZhou
 *
 */
public interface MessageTemplate {
	/**
	 * 获取消息模板
	 * @return
	 */
	public String getTemplate();
	
	/**
	 * 创建一个消息
	 * @return
	 */
	public String createMessage(Object... args);
}

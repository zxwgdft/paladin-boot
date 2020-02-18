package com.paladin.framework.utils.template;

import java.util.Formatter;

/**
 * 实质是{@link java.util.Formatter}的封装，详情看{@link java.util.Formatter}
 * @author TontoZhou
 *
 */
public class SimpleMessageTemplate implements MessageTemplate {
	
	private String template;
	
	public SimpleMessageTemplate(String template)
	{
		this.template=template;
	}
	
	@Override
	public String getTemplate() {
		return template;
	}

	@SuppressWarnings("resource")
	@Override
	public String createMessage(Object... args) {
		return new Formatter().format(template, args).toString();
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}

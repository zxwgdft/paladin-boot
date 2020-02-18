package com.paladin.common.core.template;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paladin.common.core.container.ConstantsContainer;

public class ConstantTagProcessor extends AbstractElementTagProcessor {

	private static final String TAG_NAME = "constant";// 标签名
	private static final int PRECEDENCE = 10000;// 优先级

	public ConstantTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, // 此处理器将仅应用于HTML模式
				dialectPrefix, // 要应用于名称的匹配前缀
				TAG_NAME, // 标签名称：匹配此名称的特定标签
				true, // 将标签前缀应用于标签名称
				null, // 无属性名称：将通过标签名称匹配
				false, // 没有要应用于属性名称的前缀
				PRECEDENCE); // 优先(内部方言自己的优先)
	}

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag, final IElementTagStructureHandler structureHandler) {
		String html = "";	
		final String enumcode = tag.getAttributeValue("enumcode");
		if(enumcode != null &&enumcode.length() >0) {
			String[] codes = enumcode.split(",");
			Object map = ConstantsContainer.getTypeChildren(codes);
			if(map != null) {
				try {
					html = objectMapper.writeValueAsString(map);
				} catch (JsonProcessingException e) {
				}
			}
		}
		html = "<div id='tonto_constant_value' style='display:none'>" + html + "</div>";
		structureHandler.replaceWith(html, false);
	}
	
	

}

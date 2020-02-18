package com.paladin.framework.utils.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;

import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.reflect.PathGetter;


/**
 * <h2>复杂对象解析消息模板</h2>
 * <p>用于复杂对象（包含子类等），根据地址寻找对应值，详见{@link PathGetter}}</p>
 * 
 * @author TontoZhou
 *
 */
public class ComplexObjectMessageTemplate extends ObjectMessageTemplate{
	
	public ComplexObjectMessageTemplate(InputStream input) throws IOException {
		super(input);
	}

	public ComplexObjectMessageTemplate(String template) {
		super(template);
	}
	
	public ComplexObjectMessageTemplate(InputStream input, char prefix) throws IOException {
		super(input, prefix);
	}

	public ComplexObjectMessageTemplate(String template, char prefix) {
		super(template, prefix);
	}
	
	
	/**
	 * 替换{key}关键字并创建新的字符串
	 * 
	 * @param msg
	 * @param obj
	 * @return
	 */
	protected String create(String msg, Object obj) {
		if (obj != null) {
			/*
			 * 为了效率使用StringBuilder拼接{key}格式前后的字符串
			 */
			
			Matcher matcher = pattern.matcher(msg);

			StringBuilder sb = new StringBuilder();

			int i = 0;
			for (String name : mapper) {
				matcher.find();
				Object value = PathGetter.get(obj, name);
				if (value != null) {
					sb.append(msg, i, matcher.start()).append(StringUtil.toString(value));
					i = matcher.end();
				} else {
					sb.append(msg, i, matcher.end());
					i = matcher.end();
				}
			}

			sb.append(msg, i, msg.length());
			return sb.toString();
		
		}
		return msg;
	}
}

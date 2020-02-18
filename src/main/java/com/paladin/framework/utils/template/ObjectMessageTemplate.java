package com.paladin.framework.utils.template;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.reflect.NameUtil;
import com.paladin.framework.utils.others.RegexUtil;

/**
 * 根据传入的{@code Object}对模板进行创建消息，可传入{@link java.util.Map}类型参数或简单JAVA类型
 * <p>
 * Map:通过key取值并替换模板中相应的值
 * </p>
 * <p>
 * 简单类型:通过key相应的get方法取值并替换模板中相应的值
 * </p>
 * 
 * @author TontoZhou
 * 
 */
public class ObjectMessageTemplate implements MessageTemplate {

	protected String template;

	public ObjectMessageTemplate(InputStream input) throws IOException {
		setTemplateInputStream(input);
	}

	public ObjectMessageTemplate(String template) {
		setTemplate(template);
	}
	
	public ObjectMessageTemplate(InputStream input, char prefix) throws IOException {
		this.prefix = prefix;
		setTemplateInputStream(input);
	}

	public ObjectMessageTemplate(String template, char prefix) {
		this.prefix = prefix;
		setTemplate(template);
	}

	@Override
	public String getTemplate() {
		return template;
	}

	@Override
	public String createMessage(Object... args) {

		String msg = template;

		if (mapper != null && mapper.size() > 0)
			for (Object obj : args)
				msg = create(msg, obj);

		return msg;
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
			if (obj instanceof Map) {
				@SuppressWarnings("rawtypes")
				Map mapObj = (Map) obj;

				Matcher matcher = pattern.matcher(msg);

				StringBuilder sb = new StringBuilder();

				int i = 0;
				for (String name : mapper) {
					matcher.find();
					Object value = mapObj.get(name);
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
			} else {
				Matcher matcher = pattern.matcher(msg);

				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (String name : mapper) {
					matcher.find();
					Object value = null;

					try {
						String methodName = (obj instanceof Annotation) ? name : NameUtil.addGet(name);
						Method getMethod = obj.getClass().getMethod(methodName);
						value = getMethod.invoke(obj);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
					}

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
		}
		return msg;
	}

	protected Pattern namePattern = null;

	protected Pattern pattern = null;

	protected Character prefix = null;

	// 模板参数map
	protected List<String> mapper;

	// 初始化
	protected void init() {
		
		/*
		 * 涉及转义问题，如果需要输入{}时候可以考虑prefix来避免{}被检验为需要替代的参数
		 * 也可以在这里进行转义动作，可日后进行修改
		 */
		if (template != null) {

			if (prefix == null) {
				namePattern = Pattern.compile("(?<=\\{)[a-z0-9A-Z_.]+?(?=\\})");
				pattern = Pattern.compile("\\{[a-z0-9A-Z_.]+?\\}");
			} else {
				String prefixStr = RegexUtil.escapeChar(prefix);
				namePattern = Pattern.compile("(?<=" + prefixStr + "\\{)[a-z0-9A-Z_.]+?(?=\\})");
				pattern = Pattern.compile(prefixStr + "\\{[a-z0-9A-Z_.]+?\\}");
			}

			mapper = new ArrayList<String>();

			Matcher matcher = namePattern.matcher(template);
			while (matcher.find())
				mapper.add(matcher.group());
		}
	}

	private final static Charset utf8 = Charset.forName("utf-8");

	public void setTemplateInputStream(InputStream input) throws IOException {
		byte[] data = new byte[input.available()];
		input.read(data);
		setTemplate(new String(data, utf8));
	}

	public void setTemplate(String template) {
		this.template = template;
		init();
	}

}

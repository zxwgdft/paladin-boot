package com.paladin.framework.utils.others;

import java.util.regex.Pattern;

/**
 * 
 * 匹配工具类
 * @author TontZhou
 *
 */
public class LikeUtil {

	/**
	 * 判断是否目标字符串模糊匹配
	 * <p>%：匹配任意字符，转义符为[%]</p>
	 * <p>?：匹配单个字符，转义符为[?]</p>
	 * 
	 * @param target	目标字符串
	 * @param like		模糊匹配
	 * @return
	 */
	public static boolean isLike(String target, String like) {
		String reg = parseLikeRegular2(like);
		return Pattern.compile(reg).matcher(target).find();
	}

	
	/**
	 * 正则替换，没找到效率更高的正则替换规则
	 * @param like
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String parseLikeRegular1(String like)
	{
		String reg = like.replaceAll("(?<!\\[)%(?!\\])", "[\\\\d\\\\D]*");
		reg = reg.replaceAll("(?<!\\[)\\?(?!\\])", "[\\\\d\\\\D]");
		reg = reg.replaceAll("(?<=\\[)%(?=\\])", "%");
		reg = "^" + reg + "$";
		return reg.replaceAll("(?<=\\[)\\?(?=\\])", "\\?");		
	}
	
	/**
	 * 字符拼接，比正则效率高
	 * @param like
	 * @return
	 */	
	private static String parseLikeRegular2(String like)
	{
		StringBuilder sb = new StringBuilder();
		char[] cs = like.toCharArray();
		int i = 0;
		sb.append('^');
		while (i < cs.length) {
			char c = cs[i];
			if (c == '[') {
				if (i + 2 < cs.length) {
					char c2 = cs[i + 1];
					char c3 = cs[i + 2];
					if (c3 == ']') {
						if (c2 == '%') {
							sb.append('%');
							i += 3;
							continue;
						} else if (c2 == '?') {
							sb.append('?');
							i += 3;
							continue;
						}
					}
				}
			} else if (c == '%') {
				sb.append("[\\d\\D]*");
				i++;
				continue;
			} else if (c == '?') {
				sb.append("[\\d\\D]");
				i++;
				continue;
			}

			sb.append(c);
			i++;

		}
		sb.append('$');
		return sb.toString();
	}
	

	
	
}

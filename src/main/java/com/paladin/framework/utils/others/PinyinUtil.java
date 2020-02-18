package com.paladin.framework.utils.others;

import com.paladin.framework.utils.reflect.NameUtil;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {

	static HanyuPinyinOutputFormat default_format = new HanyuPinyinOutputFormat();
	static {
		// 没有音节
		default_format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		// u => v
		default_format.setVCharType(HanyuPinyinVCharType.WITH_V);
	}

	/**
	 * 转换为汉语拼音的首字母组合，忽略非汉语
	 * <p>
	 * 例如：我是谁?谁是我 => wssssw
	 * </p>
	 * 
	 * @param str
	 * @return
	 */
	public static String toHanyuPinyinFirstArray(String str) {

		if (str == null || str.length() == 0) {
			return null;
		}

		char[] cs = str.toCharArray();
		char[] my = new char[cs.length];
		int i = 0;
		for (char c : cs) {
			try {
				String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, default_format);
				if (pinyin != null && pinyin.length > 0) {
					my[i++] = pinyin[0].charAt(0);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
			}
		}
		return new String(my, 0, i);
	}

	/**
	 * 转换为驼峰式汉语拼音
	 * <p>例如：我是谁 => woShiShui</p>
	 * 
	 * @param str
	 * @return
	 */
	public static String toHanyuPinyin(String str) {
		return toHanyuPinyin(str,true);
	}
	
	/**
	 * 转换为汉语拼音
	 * @param str
	 * @param firstUpper
	 * @return
	 */
	public static String toHanyuPinyin(String str, boolean firstUpper) {
		if (str == null || str.length() == 0) {
			return null;
		}

		char[] cs = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (char c : cs) {
			try {
				String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, default_format);
				if (pinyin != null && pinyin.length > 0) {
					if(firstUpper) {
						if (isFirst) {
							sb.append(pinyin[0]);
							isFirst = false;
						} else {
							sb.append(NameUtil.firstUpperCase(pinyin[0]));
						}
					} else {
						sb.append(pinyin[0]);
					}
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
			}
		}

		return sb.toString();
	}

}

package com.paladin.framework.utils.others;

/**
 * 正则相关工具
 */
public class RegexUtil {

    public final static char[] escape_chars = new char[]{'$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|'};

    /**
     * 是否是转义字符
     *
     * @param ch
     * @return
     */
    public static boolean isEscapeChar(char ch) {
        for (char c : escape_chars) {
            if (c == ch)
                return true;
        }

        return false;
    }

    /**
     * 转义字符串
     *
     * @param str
     * @return
     */
    public static String escapeChar(String str) {

        StringBuilder sb = new StringBuilder();

        char[] cs = str.toCharArray();

        for (char c : cs)
            sb.append(escapeChar(c));

        return sb.toString();
    }

    /**
     * 转义字符
     *
     * @param c
     * @return
     */
    public static String escapeChar(char c) {

        if (isEscapeChar(c))
            return "\\" + c;
        return "" + c;
    }

}

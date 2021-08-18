package com.paladin.framework.web.convert;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 根据日期字符串长度自动选择解析模式（简单高效的全局处理日期格式与带时分秒格式参数的解析）
 */
public class AutoDateFormat extends SimpleDateFormat {

    private SimpleDateFormat shortDateFormat;

    public AutoDateFormat() {
        super("yyyy-MM-dd HH:mm:ss");
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        setTimeZone(timeZone);

        shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        shortDateFormat.setTimeZone(timeZone);
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        if (source.length() > 10) {
            return super.parse(source, pos);
        } else {
            return shortDateFormat.parse(source, pos);
        }
    }

    @Override
    public Object clone() {
        // 因为线程不安全，所以在每次处理时会克隆一个对象进行解析
        AutoDateFormat other = (AutoDateFormat) super.clone();
        other.shortDateFormat = (SimpleDateFormat) shortDateFormat.clone();
        return other;
    }


}

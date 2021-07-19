package com.paladin.framework.web.convert;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;


public class DateFormatter implements Formatter<Date> {

    private final static ThreadLocal<AutoDateFormat> dateFormat = new ThreadLocal() {
        public AutoDateFormat initialValue() {
            return new AutoDateFormat();
        }
    };

    @Override
    public String print(Date object, Locale locale) {
        // 一般都通过jackson返回json格式数据，所以这里被使用很少
        if (object != null) {
            return dateFormat.get().format(object);
        }
        return null;
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        if (text != null) {
            text = text.trim();
            if (text.length() == 0) {
                return null;
            }
            return dateFormat.get().parse(text);
        }
        return null;
    }

}

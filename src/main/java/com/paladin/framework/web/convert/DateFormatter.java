package com.paladin.framework.web.convert;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateFormatter implements Formatter<Date> {

    private final static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>() {
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    @Override
    public String print(Date object, Locale locale) {
        if (object != null) {
            return timeFormat.get().format(object);
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

            if (text.length() > 10) {
                try {
                    return timeFormat.get().parse(text);
                } catch (ParseException e) {
                }
            } else {
                try {
                    return dateFormat.get().parse(text);
                } catch (ParseException e) {
                }
            }
        }
        return null;
    }

}

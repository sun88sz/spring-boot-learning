package com.sun.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Slf4j
public class StringToDateConverter implements Converter<String, Date> {

    private static final List<String> formarts = new ArrayList<>(4);

    static {
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd hh:mm");
        formarts.add("yyyy-MM-dd hh:mm:ss");
        formarts.add("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    @Override
    public Date convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, formarts.get(0));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, formarts.get(1));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formarts.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formarts.get(3));
        } else if (source.contains("T")) {
            try {
                return parseDateZone(source, formarts.get(4), ZoneId.of("UTC"));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
            }
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }

    /**
     * 格式化日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     * @return Date 日期
     */
    private Date parseDate(String dateStr, String format) {
        return parseDateZone(dateStr, format, ZoneId.systemDefault());
    }

    private Date parseDateZone(String dateStr, String format, ZoneId zoneId) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setTimeZone(TimeZone.getTimeZone(zoneId));
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            log.error("自动转换时间错误", e);
        }
        return date;
    }
}
package com.sun.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Slf4j
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final List<String> formarts = new ArrayList<>(4);

    static {
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd hh:mm");
        formarts.add("yyyy-MM-dd hh:mm:ss");
        formarts.add("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    @Override
    public LocalDateTime convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseLocalDateTime(source, formarts.get(0));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseLocalDateTime(source, formarts.get(1));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseLocalDateTime(source, formarts.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseLocalDateTime(source, formarts.get(3));
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

    private LocalDateTime parseLocalDateTime(String dateStr, String format) {
        return parseDateZone(dateStr, format, ZoneId.systemDefault());
    }

    private LocalDateTime parseDateZone(String dateStr, String format, ZoneId zoneId) {
        LocalDateTime dateTime = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setTimeZone(TimeZone.getTimeZone(zoneId));
            dateTime = dateFormat.parse(dateStr).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

}
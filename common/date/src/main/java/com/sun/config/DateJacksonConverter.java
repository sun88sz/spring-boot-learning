package com.sun.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateJacksonConverter extends JsonDeserializer<Date> {
    private static String[] pattern =
            new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S",
                    "yyyy.MM.dd", "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss.S",
                    "yyyy/MM/dd", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss.S", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"};

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        Date targetDate = null;
        String originDate = p.getText();
        if ("".equals(originDate)) {
            return null;
        }
        if (StringUtils.isNotEmpty(originDate)) {
            try {
                long longDate = Long.parseLong(originDate.trim());
                targetDate = new Date(longDate);
            } catch (NumberFormatException e) {
                try {
                    if (originDate.contains("T")) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        targetDate = dateFormat.parse(originDate);
                    } else {
                        targetDate = DateUtils.parseDate(originDate, DateJacksonConverter.pattern);
                    }
                } catch (ParseException pe) {
                    throw new IOException(String.format(
                            "'%s' can not convert to type 'java.util.Date',just support timestamp(type of long) and following date format(%s)",
                            originDate,
                            StringUtils.join(pattern, ",")));
                }
            }
        }
        return targetDate;
    }

    @Override
    public Class<?> handledType() {
        return LocalDateTime.class;
    }
}
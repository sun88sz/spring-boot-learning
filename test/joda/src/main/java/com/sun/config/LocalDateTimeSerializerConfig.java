package com.sun.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class LocalDateTimeSerializerConfig {

    @Value("${spring.jackson.date-format:yyyy-MM-dd___HH:mm:ss}")
    private String pattern;

   private TimeZone timeZone =  TimeZone.getTimeZone("Asia/Shanghai");

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd***HH:mm:ss");

    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withZone(timeZone.toZoneId()).withDecimalStyle(DecimalStyle.STANDARD);
//        return new LocalDateTimeSerializer(dateTimeFormatter);
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }


    @Bean
    public LocalDateTimeTimestampSerializer localDateTimeTimestampDeserializer() {
        return new LocalDateTimeTimestampSerializer();
    }


    @Bean
    public DateSerializer dateDeserializer() {
        // 会格式化返回时间戳
//        return new DateSerializer(true, simpleDateFormat);
        simpleDateFormat.setTimeZone(timeZone);
        return new DateSerializer(false, simpleDateFormat);
    }


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(Date.class, dateDeserializer());

            // 使用yyyy-MM-dd ...格式
//            builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
            // 使用 时间戳格式 会覆盖 @JsonFormat
            builder.serializerByType(LocalDateTime.class, localDateTimeTimestampDeserializer());
        };
    }

}

class LocalDateTimeTimestampSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }
}

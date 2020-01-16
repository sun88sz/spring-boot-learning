package com.sun.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.joda.money.Money;
import org.joda.time.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
public class JodaModel {

    private String product;
    private Money money;

    private Date date;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date2;

    private LocalDateTime localDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime2;

    private LocalDate localDate;
    private LocalTime localTime;

    private DateTime dateTime;

}

package com.sun.controller;

import com.sun.bean.JodaModel;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@RestController
public class JodaController {

    @PostMapping("/xx")
    public JodaModel xx(@RequestBody JodaModel request) {

        System.out.println(request);

        JodaModel jodaModel = new JodaModel();
        jodaModel.setMoney(Money.of(CurrencyUnit.of("CNY"), new BigDecimal("123456.00")));
        jodaModel.setProduct("TYUIUOI");

        jodaModel.setDate(new Date());
        jodaModel.setDate2(new Date());

        jodaModel.setDateTime(new DateTime());

        jodaModel.setLocalDate(LocalDate.now());
        jodaModel.setLocalTime(LocalTime.now());

        jodaModel.setLocalDateTime(LocalDateTime.now());
        jodaModel.setLocalDateTime2(LocalDateTime.now());

        return jodaModel;
    }
}

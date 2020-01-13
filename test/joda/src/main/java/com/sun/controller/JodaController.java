package com.sun.controller;

import com.sun.bean.JodaModel;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class JodaController {

    @PostMapping("/xx")
    public JodaModel xx(@RequestBody JodaModel request) {

        System.out.println(request);


        JodaModel jodaModel = new JodaModel();
        jodaModel.setMoney(Money.of(CurrencyUnit.of("CNY"), new BigDecimal("123456.00")));
        jodaModel.setProduct("TYUIUOI");

        return jodaModel;
    }
}

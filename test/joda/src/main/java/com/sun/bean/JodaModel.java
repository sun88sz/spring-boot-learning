package com.sun.bean;

import lombok.Data;
import org.joda.money.Money;

@Data
public class JodaModel {

    private String product;
    private Money money;
}

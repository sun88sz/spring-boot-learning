package com.sun;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Employee implements Serializable {

    private Long id;

    private String name;

    private BigDecimal money;

    private List<String> uuid;

    private Double height;

    private Date date;

}

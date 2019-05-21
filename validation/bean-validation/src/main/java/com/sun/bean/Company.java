package com.sun.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
public class Company implements Serializable {

    private Long id;

    @Length(min = 5, max = 10, message = "姓名" )
    private String name;
}

package com.sun.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String employeeName;

}
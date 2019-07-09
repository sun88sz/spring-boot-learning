package com.sun.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Employee implements Serializable {

//    @com.sun.bean.customize.NotNull(error = ErrorCode.NOT_NULL)
//    private Long code;

    @NotNull(message = "xxxx")
    private Long id;



}

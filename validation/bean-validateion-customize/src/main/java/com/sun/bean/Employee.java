package com.sun.bean;

import com.sun.validation.constraints.Length;
import com.sun.validation.constraints.NotNull;
import com.sun.validation.constraints.decimal.DecimalMax;
import com.sun.validation.constraints.decimal.DecimalMin;
import lombok.Data;

import java.io.Serializable;

import static com.sun.config.ValidationConfig.MAX_LENGTH;

@Data
public class Employee implements Serializable {

    @NotNull(property = "主键")
    private Long id;

    @Length(property = "名称", max = MAX_LENGTH)
    private String name;

    @NotNull(property = "钱")
    @DecimalMin(property = "钱", value = "1")
    @DecimalMax(property = "钱", value = "10")
    private Double money;

}

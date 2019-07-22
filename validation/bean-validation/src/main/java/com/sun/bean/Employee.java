package com.sun.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
public class Employee implements Serializable {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotNull(message = "年龄不能为空")
    @Range(min = 0, max = 200)
    private Integer age;

    @DecimalMin("0.0")
    @DecimalMax("9999.99")
    private Double salary;

    // 空字符串也不行
    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotEmpty(message = "EC_210101")
    private List<String> position;

    @Valid
    private Company company;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

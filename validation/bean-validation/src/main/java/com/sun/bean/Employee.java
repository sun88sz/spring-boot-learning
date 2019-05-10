package com.sun.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class Employee implements Serializable {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotNull(message = "年龄不能为空")
    private Integer age;

    // 空字符串也不行
    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotEmpty(message = "职位不能为空")
    private List<String> position;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

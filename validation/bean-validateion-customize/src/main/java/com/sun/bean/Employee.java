package com.sun.bean;

import com.sun.consts.ValidationConstant;
import com.sun.validation.constraints.Length;
import com.sun.validation.constraints.NotNull;
import com.sun.validation.constraints.Size;
import com.sun.validation.constraints.decimal.DecimalMax;
import com.sun.validation.constraints.decimal.DecimalMin;
import com.sun.validation.constraints.maxmin.Max;
import com.sun.validation.constraints.maxmin.Min;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class Employee implements Serializable {

    @NotNull(property = "主键")
    private Long id;

    @Length(property = "名称", max = ValidationConstant.NAME_MAX_LENGTH)
    private String name;

    @NotNull(property = "钱")
    @DecimalMin(property = "钱", value = ValidationConstant.MONEY_MIN + "")
    @DecimalMax(property = "钱", value = ValidationConstant.MONEY_MAX + "")
    private BigDecimal money;

    @Size(property = "爱好", min = 1, max = 3)
    private List<String> uuid;

    @Max(property = "身高", value = 3.5, message = "犯规了！最大->{value}!!!!!!")
    @Min(property = "身高", value = 0.5)
    private Double height;


}

package com.sun.bean;

import com.sun.consts.ValidationConstant;
import com.sun.validation.constraints.Length;
import com.sun.validation.constraints.NotNull;
import com.sun.validation.constraints.Size;
import com.sun.validation.constraints.decimal.DecimalMax;
import com.sun.validation.constraints.decimal.DecimalMin;
import com.sun.validation.constraints.maxmin.Max;
import com.sun.validation.constraints.maxmin.Min;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Employee implements Serializable {

    @NotNull
    @ApiModelProperty(name = "主键id")
    private Long id;

    @Length(property = "名称", max = ValidationConstant.NAME_MAX_LENGTH)
    private String name;

    @ApiModelProperty(name = "钱..")
    @NotNull
    @DecimalMin(ValidationConstant.MONEY_MIN + "")
    @DecimalMax(ValidationConstant.MONEY_MAX + "")
    private BigDecimal money;

    @Size(property = "爱好", min = 1, max = 3)
    private List<String> uuid;

    @ApiModelProperty("身高")
    @Max(value = ValidationConstant.HEIGHT_MAX, message = "犯规了！最大->{value}!!!!!!")
    @Min(value = ValidationConstant.HEIGHT_MIN)
    private Double height;

    private Date date;

}

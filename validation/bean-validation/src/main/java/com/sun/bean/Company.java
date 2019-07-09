package com.sun.bean;

import com.sun.bean.validate.CreateGroup;
import com.sun.bean.validate.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Company implements Serializable {

    @NotNull(groups = {UpdateGroup.class})
    private Long id;

    @NotNull(groups = {UpdateGroup.class, CreateGroup.class})
    @Length(min = 5, max = 10, message = "姓名")
    private String name;
}

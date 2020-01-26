package com.sun.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sun
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    private Integer code;
    private String name;

}

package com.sun.mq;

import lombok.Data;

import java.io.Serializable;

/**
 * Description: <br/>
 * Date: 2018-08-26
 *
 * @author Sun
 */
@Data
public class User implements Serializable {


    private static final long serialVersionUID = -7153017225542746634L;
    private Long id;
    private String name;
    private Integer age;
    private String phone;
}

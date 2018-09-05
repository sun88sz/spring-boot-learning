package com.sun.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Random;

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



    public static User createUser() {
        User user = new User();
        Random random = new Random();
        int i = random.nextInt();
        long l = random.nextLong();
        user.setAge(i);
        user.setId(l);
        user.setName("name");

        return user;
    }
}

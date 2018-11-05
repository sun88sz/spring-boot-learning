package com.sun;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author : Sun
 * @date : 2018/11/5 16:27
 */
@Service
public class UserService {
    
    @Value("${name}")
    private String name;
    
    @Value("${age}")
    private Integer age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

package com.sun.company;

import com.sun.version.ApiVersion;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Sun
 * @date : 2018/9/13 15:16
 */
@ApiVersion
@RestController
@RequestMapping("/company")
public class CompanyController {
    
    @GetMapping("/test1")
    public String test1(){
        return "test1 v0";
    }

    @GetMapping("/test2")
    public String test2(){
        return "test2 v0";
    }

    @GetMapping("/test3")
    public String test3(){
        return "test3 v0";
    }
}

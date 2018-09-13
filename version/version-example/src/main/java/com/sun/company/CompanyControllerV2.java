package com.sun.company;

import com.sun.version.ApiVersion;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Sun
 * @date : 2018/9/13 15:17
 */
@ApiVersion(2)
@RestController
@RequestMapping("/{version}/company")
public class CompanyControllerV2 {
    
    @GetMapping("/test3")
    public String test3(){
        return "test3 v2";
    }
}
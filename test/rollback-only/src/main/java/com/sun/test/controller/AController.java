package com.sun.test.controller;

import com.sun.test.service.AService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AController {

    @Autowired
    AService aService;

    @GetMapping("/a")
    public void a(){
        aService.a();
    }
}

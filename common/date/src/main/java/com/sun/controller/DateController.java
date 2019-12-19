package com.sun.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DateController {

    @PostMapping("/")
    public User postUser(@RequestBody User user) {
        return user;
    }
}

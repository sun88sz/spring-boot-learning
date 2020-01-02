package com.sun.controller;

import com.sun.bean.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@Controller
@RequestMapping("/emp")
public class EmployeeController {

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping("/save")
    public ResponseEntity save(@Validated @RequestBody Employee employee) {
        return ResponseEntity.ok().body(employee);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping("/test")
    public ResponseEntity test( @RequestBody Employee employee) {
        return ResponseEntity.ok().body(employee);
    }

}

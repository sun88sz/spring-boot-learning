package com.sun.controller;

import com.sun.bean.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/emp")
public class EmployeeController {


    @RequestMapping("/save")
    public ResponseEntity xx(@Validated @RequestBody Employee employee) {

        log.info("Id: {}", employee.getId());
        log.info("Age: {}", employee.getAge());
        log.info("Name: {}", employee.getName());
        log.info("Position: {}", employee.getPosition());

        return ResponseEntity.ok().body(employee);
    }
}

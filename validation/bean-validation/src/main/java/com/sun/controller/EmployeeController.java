package com.sun.controller;

import com.sun.bean.Employee;
import com.sun.bean.validate.CreateGroup;
import com.sun.bean.validate.UpdateGroup;
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
    public ResponseEntity save(@Validated(CreateGroup.class) @RequestBody Employee employee) {
        return ResponseEntity.ok().body(employee);
    }


    @RequestMapping("/update")
    public ResponseEntity update(@Validated(UpdateGroup.class) @RequestBody Employee employee) {
        return ResponseEntity.ok().body(employee);
    }
}

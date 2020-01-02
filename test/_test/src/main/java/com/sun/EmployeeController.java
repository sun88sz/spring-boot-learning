package com.sun;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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


    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse("2019-1-1");
        System.out.println(parse);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDate date = LocalDate.parse("2019-1-1", dateTimeFormatter);
        System.out.println(date);
    }
}

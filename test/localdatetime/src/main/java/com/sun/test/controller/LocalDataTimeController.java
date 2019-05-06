package com.sun.test.controller;

import com.sun.test.vo.LocalDateTimeVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class LocalDataTimeController {

    @GetMapping("/get")
    public LocalDateTimeVo get() {
        LocalDateTimeVo localDateTimeVo = new LocalDateTimeVo();
        localDateTimeVo.setDate(LocalDateTime.now().withNano(0));
        localDateTimeVo.setJacksonDate(LocalDateTime.now());
        localDateTimeVo.setSpringDate(LocalDateTime.now());
        localDateTimeVo.setId(1L);
        localDateTimeVo.setName("xxxxxxxxxx");
        return localDateTimeVo;
    }

    @PostMapping("/post")
    public LocalDateTimeVo post(@RequestBody LocalDateTimeVo vo) {
        System.out.println(vo.getDate());
        System.out.println(vo.getJacksonDate());
        System.out.println(vo.getSpringDate());
        return vo;
    }
}

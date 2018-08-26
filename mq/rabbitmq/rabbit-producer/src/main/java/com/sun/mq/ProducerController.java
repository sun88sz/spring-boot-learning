package com.sun.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: <br/>
 * Date: 2018-08-26
 *
 * @author Sun
 */
@RestController
@RequestMapping
public class ProducerController {

    @Autowired
    private Producer1 producer1;

    @GetMapping(path = "/test")
    public User test() {
        return producer1.test();
    }
}

package com.sun.mq;

import com.sun.bean.User;

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
    @Autowired
    private Producer2 producer2;

    @GetMapping(path = "/test")
    public User test() {
        return producer1.test();
    }


    @GetMapping(path = "/test2")
    public User test2() {
        return producer1.test2();
    }

    
    @GetMapping(path = "/test3")
    public User test3() {
        return producer2.test();
    }


    @GetMapping(path = "/test4")
    public User test4() {
        return producer2.test2();
    }
}

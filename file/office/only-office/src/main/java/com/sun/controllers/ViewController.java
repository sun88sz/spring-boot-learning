package com.sun.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/editor")
    public String editor() {
        return "editor";
    }
}

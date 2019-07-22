package com.sun;

import com.sun.component.AppListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TranscodingApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TranscodingApplication.class);
        app.addListeners(new AppListener());
        app.run(args);
    }

}
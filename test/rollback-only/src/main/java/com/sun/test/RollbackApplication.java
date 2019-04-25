package com.sun.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RollbackApplication {

	public static void main(String[] args) {
        SpringApplication.run(RollbackApplication.class, args);
	}

}
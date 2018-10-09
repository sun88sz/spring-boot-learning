package com.sun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * 
 * @author Sun
 */
@Configuration
public class RouterConfiguration {
    
    @Autowired
    private UserController userController;

    @Bean
    public RouterFunction<?> routerFunction() {
        return RouterFunctions.route(RequestPredicates.GET("/time"), userController::sendTimePerSec);
    }
}

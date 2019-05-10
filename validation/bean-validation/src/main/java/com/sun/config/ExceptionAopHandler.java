package com.sun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 验证框架错误处理
 */
@Configuration
@ControllerAdvice("com.sun")
public class ExceptionAopHandler {

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<String> validatorErrorHandler(BindException bindException) throws Exception {
        List<FieldError> errors = bindException.getBindingResult().getFieldErrors();
        StringBuilder message = new StringBuilder();
        for (int i = 0, size = errors.size(); i < size; i++) {
            FieldError fieldError = errors.get(i);
            message.append(fieldError.getDefaultMessage());
            message.append("\r\n");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message.toString());
    }
}

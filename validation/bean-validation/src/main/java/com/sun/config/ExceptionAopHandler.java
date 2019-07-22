package com.sun.config;

import com.sun.exception.ErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证框架错误处理
 */
@Configuration
@ControllerAdvice("com.sun")
public class ExceptionAopHandler {

    public static Map<String, String> errorMap = new HashMap<>();

    static {
        errorMap.put("EC_210101", ErrorCodes.EC_210101.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> validatorErrorHandler(MethodArgumentNotValidException bindException) {
        List<FieldError> errors = bindException.getBindingResult().getFieldErrors();
        StringBuilder message = new StringBuilder();
        for (int i = 0, size = errors.size(); i < size; i++) {
            FieldError fieldError = errors.get(i);
            String defaultMessage = fieldError.getDefaultMessage();

            if (StringUtils.isNotBlank(defaultMessage)) {
                if (defaultMessage.startsWith("EC_")) {
                    message.append(errorMap.get(fieldError.getDefaultMessage()));
                } else {
                    message.append(fieldError.getDefaultMessage());
                }
                message.append("\r\n");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message.toString());
    }
}

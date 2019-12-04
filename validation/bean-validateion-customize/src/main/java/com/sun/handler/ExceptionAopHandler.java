package com.sun.handler;

import com.sun.exception.BusinessException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    /**
     * @param bindException
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> validatorValidationErrorHandler(MethodArgumentNotValidException bindException) {
        List<FieldError> errors = bindException.getBindingResult().getFieldErrors();
        StringBuilder message = new StringBuilder();
        for (int i = 0, size = errors.size(); i < size; i++) {
            FieldError fieldError = errors.get(i);

            // TODO
            // 注解名称
            // 多个字段有多种验证
            // 一个字段可能多种验证
            String code = fieldError.getCode();
            System.out.println(code);


            message.append(fieldError.getDefaultMessage());
            message.append("\r\n");
        }
        throw BusinessException.build(10000, message.toString());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message.toString());
    }


    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<String> validatorBusinessErrorHandler(BusinessException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getErrorCode().getCode() + ":" + exception.getErrorCode().getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> validatorErrorHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

}

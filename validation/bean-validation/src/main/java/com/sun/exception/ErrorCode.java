package com.sun.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorCode implements Serializable {

    @Getter
    public Integer code;

    @Getter
    public String message;

    public ErrorCode(String message) {
        this.message = message;
    }
}

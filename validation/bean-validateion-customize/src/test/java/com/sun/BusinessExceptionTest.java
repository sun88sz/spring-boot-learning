package com.sun;

import com.sun.exception.BusinessException;
import com.sun.exception.ErrorCode;
import org.junit.Test;

public class BusinessExceptionTest {

    @Test
    public void build() {
        ErrorCode error = new ErrorCode(111111, "aaaaa [%s] bbbbb [%s]");
        BusinessException build = BusinessException.build(error, "1", "2");
        System.out.println(build.getErrorCode().getMessage());


        ErrorCode error2 = new ErrorCode(111111, "cccccc [%s] ");
        BusinessException build2 = BusinessException.build(error2, "3", "4");
        System.out.println(build2.getErrorCode().getMessage());

    }
}

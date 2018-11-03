package com.sun;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author : Sun
 * @date : 2018/10/11 10:02
 */
public class Local {

    static TransmittableThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();


    public static void main(String[] args) {

    }
}

package com.sun.redisson.example;

import java.util.HashMap;

import static com.sun.redisson.aop.SyncLockAspect.readExpr;

public class ELTest {

    public static final String xxx = "X";
    public static final String yyyy = "Y";


    public static void main(String[] args) {
        LockParameter parameter = new LockParameter();
        parameter.setId(1L);
        parameter.setName("name");


        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("parameter", parameter);

        String s = readExpr("'" + xxx + "_'+#parameter.id+':" + yyyy + "_'+#parameter.name", stringObjectHashMap);
        System.out.println(s);
    }


}

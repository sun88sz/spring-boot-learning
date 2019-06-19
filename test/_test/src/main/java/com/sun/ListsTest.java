package com.sun;

import com.google.common.collect.Lists;

import java.util.ArrayList;

public class ListsTest {

    public static void main(String[] args) {

        ArrayList<Integer> objects = Lists.newArrayList();
        for (int i = 0; i < 200000L; i++) {
            objects.add(i);
        }


        long l1 = System.currentTimeMillis();
        for (Integer i : objects) {
            System.out.print(i);
        }
        long l2 = System.currentTimeMillis();

        System.out.println();
        System.out.println(l2 - l1);



        int size = objects.size();
        long   l3 = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            Integer integer = objects.get(i);
            System.out.print(integer);
        }
        long   l4 = System.currentTimeMillis();
        System.out.println();

        System.out.println(l4 - l3);


    }
}

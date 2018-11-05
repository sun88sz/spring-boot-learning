package com.sun;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author : Sun
 * @date : 2018/11/5 16:46
 */
@SpringBootTest(classes = ValueAnnotationApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AnnoTest {


    @Autowired
    private UserService user;

    @Test
    public void test() {


        System.out.println(user.getAge());
        System.out.println(user.getName());

    }
}

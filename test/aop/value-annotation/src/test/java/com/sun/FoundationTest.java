package com.sun;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author : Sun
 * @date : 2018/11/7 16:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FoundationTest {
    
    @Autowired
    private ValueTest value;
    
    @Test
    public void xx(){

        System.out.println("---------------------------------------------------------");
        System.out.println(value.getA());
        System.out.println(value.getB());
        System.out.println(value.getC());
        System.out.println(value.getD());

//        String value = SystemConstantServiceUtil.getValue("aa.cc");

        System.out.println(value);


        System.out.println(TestConst.AA); 
        System.out.println(TestConst.BB); 
        
//        SystemConstantServiceUtil.initConstantClass(TestConst.class);

        System.out.println(TestConst.AA);
        System.out.println(TestConst.BB);

   }
}

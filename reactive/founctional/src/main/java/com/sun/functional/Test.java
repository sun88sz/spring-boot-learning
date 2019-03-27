package com.sun.functional;

import java.io.File;
import java.util.function.Function;

/**
 * @author Administrator
 */
public class Test {

    public static void main(String[] args) {

        Test test = new Test();
        test.xx(test::export, new ExportParameter());


    }


    public void xx(Function<ExportParameter, File> function, ExportParameter parameter) {
        File apply = function.apply(parameter);

        System.out.println(apply);
    }


    public File export(ExportParameter parameter) {
        System.out.printf("xxxx");
        return null;
    }
}

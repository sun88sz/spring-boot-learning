package com.sun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {


        FileInputStream fis = new FileInputStream(new File("D:/3.doc"));

        long b = System.currentTimeMillis();

        Excel2Html.readExcelToHtml(fis, "D:/", "3.doc_poi.html", true);

        long e = System.currentTimeMillis();

        System.out.println(e - b);
    }
}

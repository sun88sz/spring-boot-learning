package com.sun.example;

import java.io.File;
import java.io.IOException;

import org.ghost4j.document.DocumentException;
import org.ghost4j.renderer.RendererException;

/**
 * @author : Sun
 * @date : 2018/10/19 18:21
 */
public class Test {

    public static void main(String[] args) throws IOException, RendererException, DocumentException {
//        File file = new File("D:/2018_PDF.pdf");
        File file = new File("D:/dudutest.pdf");

        long l = System.currentTimeMillis();

        Pdfbox.export(file, 75, 3);

        long l2 = System.currentTimeMillis();

//        Ghost4j.export(file, 60, 3);

        long l3 = System.currentTimeMillis();

        System.out.println(l2 - l);
        System.out.println(l3 - l2);
    }
}

package com.sun;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * @author : Sun
 * @date : 2018/10/19 18:10
 */
public class Pdfbox {

    public static void export(File file, int dpi, int maxSize) throws IOException {


        FileInputStream fis = new FileInputStream(file);

        // pdf附件
        PDDocument document = PDDocument.load(fis, MemoryUsageSetting.setupMainMemoryOnly());
        int pageSize = document.getNumberOfPages();
        PDFRenderer reader = new PDFRenderer(document);

        int limitPageSize = Math.min(pageSize, maxSize);

        for (int i = 0; i < limitPageSize; i++) {

            int dpiPage = dpi;
            PDPage page = document.getPage(i);

            PDRectangle mediaBox = page.getMediaBox();
            float height = mediaBox.getHeight();
            float width = mediaBox.getWidth();
            if (height > 5000) {
                dpiPage = 20;
            } else if (height > 2000) {
                dpiPage = 45;
            } else if (height > 1000) {
                dpiPage = 60;
            }

            BufferedImage bufferedImage = reader.renderImageWithDPI(i, dpiPage);


            // 使用png的清晰度
            ImageIO.write(bufferedImage, "png", new File("D:/Pdfbox" + (i + 1) + ".png"));
        }
    }

    public static void main(String[] args) throws IOException {
    }


}

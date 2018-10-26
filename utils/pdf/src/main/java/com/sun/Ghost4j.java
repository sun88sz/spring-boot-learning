package com.sun;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;

/**
 * @author : Sun
 * @date : 2018/10/19 17:08
 */
public class Ghost4j {

    /**
     * @param file
     * @throws IOException
     * @throws DocumentException
     * @throws RendererException
     */
    public static void export(File file, int dpi, int maxSize) throws IOException, DocumentException, RendererException {


        PDFDocument document = new PDFDocument();
        document.load(file);
        
        SimpleRenderer renderer = new SimpleRenderer();
        // set resolution (in DPI)
        renderer.setResolution(dpi);

        int pageSize = document.getPageCount();
        int limitPageSize = Math.min(pageSize, maxSize);

        List<Image> images = renderer.render(document, 0, limitPageSize);
        
        for (int i = 0; i < limitPageSize; i++) {
            ImageIO.write((RenderedImage) images.get(i), "png", new File("D:/Ghost4j" + (i + 1) + ".png"));
        }
    }

    public static void main(String[] args) throws IOException, RendererException, DocumentException {
        export(new File("D:/2018_PDF.pdf"), 75, 5);
    }
}

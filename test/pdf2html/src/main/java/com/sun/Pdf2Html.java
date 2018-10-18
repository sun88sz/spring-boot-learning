package com.sun;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * @author : Sun
 * @date : 2018/10/18 09:37
 */

public class Pdf2Html {

    public static synchronized boolean pdfToImage(InputStream input, String outPath, String htmlFileName) {

        StringBuffer buffer = new StringBuffer();
        FileOutputStream fos = null;
        PDDocument document = null;
        int size = 0;
        BufferedImage image = null;
        FileOutputStream out = null;
        File file = new File(outPath + "/" + htmlFileName);
        if (file.exists()) {
            return true;
        }
        boolean flag = false;

        // PDF转换成HTML保存的文件夹
        File htmlsDir = new File(outPath);
        if (!htmlsDir.exists()) {
            htmlsDir.mkdirs();
        }
        try {
            // 遍历处理pdf附件
            buffer.append("<!doctype html>\r\n");
            buffer.append("<head>\r\n");
            buffer.append("<meta charset=\"UTF-8\">\r\n");
            buffer.append("</head>\r\n");
            buffer.append("<body style=\"background-color:gray;\">\r\n");
            buffer.append("<style>\r\n");
            buffer.append(
                    "img {background-color:#fff; text-align:center; width:100%; max-width:100%;margin-top:6px;}\r\n");
            buffer.append("</style>\r\n");

            // pdf附件
            document = PDDocument.load(input);
            size = document.getNumberOfPages();
            PDFRenderer reader = new PDFRenderer(document);

            for (int i = 0; i < size; i++) {
                // image = new PDFRenderer(document).renderImageWithDPI(i,130,ImageType.RGB);
                image = reader.renderImage(i, 1.5f);
                // 生成图片,保存位置
                out = new FileOutputStream(outPath + "/" + "image" + "_" + i + ".jpg");
                ImageIO.write(image, "png", out); // 使用png的清晰度
                // 将图片路径追加到网页文件里
                buffer.append("<img src=\"image" + "_" + i + ".jpg\"/>\r\n");
                image = null;
                out.flush();
                out.close();
            }
            reader = null;
            document.close();
            buffer.append("</body>\r\n");
            buffer.append("</html>");
            // 生成网页文件
            fos = new FileOutputStream(file);
            fos.write(buffer.toString().getBytes());
            fos.flush();
            fos.close();
            buffer.setLength(0);
            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (document != null) {
                    document.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }


    public static void main(String[] args) throws FileNotFoundException {
//        FileInputStream fis = new FileInputStream(new File("D:/2018_PDF.pdf"));
        FileInputStream fis = new FileInputStream(new File("D:/企业云盘移动流程图.pdf"));
        pdfToImage( fis,"D:/","xxx");
    }

}

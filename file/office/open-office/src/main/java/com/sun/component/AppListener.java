package com.sun.component;

import lombok.extern.slf4j.Slf4j;
import org.jodconverter.DocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.document.DocumentFormat;
import org.jodconverter.document.DocumentFormatRegistry;
import org.jodconverter.office.OfficeException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
public class AppListener implements ApplicationListener<ApplicationReadyEvent> {


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext context = event.getApplicationContext();
        DocumentConverter converter = context.getBean(DocumentConverter.class);

        try {
            FileInputStream fis = new FileInputStream(new File("D:/3.xlsx"));

            long b = System.currentTimeMillis();
//            ConversionJobWithOptionalSourceFormatUnspecified convert = converter.convert(new File("D:/2.xlsx"));

            // 2:获取Format
            DocumentFormatRegistry factory = DefaultDocumentFormatRegistry.getInstance();
            DocumentFormat inputDocumentFormat = factory.getFormatByExtension("xlsx");
            converter.convert(fis, true).as(inputDocumentFormat).to(new File("D:/3_open.html")).execute();
            long e = System.currentTimeMillis();

            long be = e - b;
            log.info("convert end ..." + be);
        } catch (OfficeException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
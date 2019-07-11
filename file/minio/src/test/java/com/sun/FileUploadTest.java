package com.sun;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.MinioApplication;
import sun.service.MinioFileService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = MinioApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class FileUploadTest {

    @Autowired
    MinioFileService minioFileService;

    @Test
    public void save() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(new File("D:/错误码统计.xlsx"));
        String uuid = minioFileService.putObject("aaa/aaa", fis, "错误码统计.xlsx", null);

        String url = minioFileService.getUrl("aaa/aaa", uuid);
        System.out.println(url);
    }
}

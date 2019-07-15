package com.sun.service;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
public class MinioFileService {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * 新增文件
     *
     * @param bucketName
     * @param is
     * @param fileName
     * @param contentType
     * @return
     */
    public String putObject(String bucketName, InputStream is, String fileName, String contentType) {
        String uuid = null;
        try {
            if (StringUtils.isBlank(fileName)) {
                throw new RuntimeException("fileName 必填");
            }

            // Create a minioClient with the MinIO Server name, Port, Access key and Secret key.
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);

            // Check if the bucket already exists.
            boolean isExist = minioClient.bucketExists(bucketName);
            if (!isExist) {
                // Make a new bucket called asiatrip to hold a zip file of photos.
                minioClient.makeBucket(bucketName);
            }
            // 文件大小
            int available = is.available();
            // contentType
            if (StringUtils.isBlank(contentType)) {
                contentType = Files.probeContentType(Paths.get(fileName));
            }

            // uuid
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            uuid = UUID.randomUUID().toString() + "." + suffix;

            // Upload the zip file to the bucket with putObject
            minioClient.putObject(bucketName, uuid, is, (long) available, null, null, contentType);
        } catch (MinioException | NoSuchAlgorithmException | IOException | XmlPullParserException | InvalidKeyException e) {
            log.error("PutObject Error : ", e);
            log.error("bucketName {}, uuid {}", bucketName, uuid);
        }
        return uuid;
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param uuid
     * @return
     */
    public boolean removeObject(String bucketName, String uuid) {
        try {
            // Create a minioClient with the MinIO Server name, Port, Access key and Secret key.
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            minioClient.removeObject(bucketName, uuid);
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException | XmlPullParserException e) {
            log.error("Minio RemoveObject Error : ", e);
            log.error("bucketName {}, uuid {}", bucketName, uuid);
            return false;
        }
        return true;
    }

    /**
     * 获取文件信息
     *
     * @param bucketName
     * @param uuid
     * @return
     */
    public ObjectStat getObjectInfo(String bucketName, String uuid) {
        try {
            // Create a minioClient with the MinIO Server name, Port, Access key and Secret key.
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            return minioClient.statObject(bucketName, uuid);
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException | XmlPullParserException e) {
            log.error("GetObjectInfo Error : ", e);
            log.error("bucketName {}, uuid {}", bucketName, uuid);
        }
        return null;
    }

    public String getUrl(String bucketName, String uuid) {
        try {
            // Create a minioClient with the MinIO Server name, Port, Access key and Secret key.
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            return minioClient.getPresignedObjectUrl(Method.GET, bucketName, uuid, 1800, null);
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException | XmlPullParserException e) {
            log.error("GetObjectInfo Error : ", e);
            log.error("bucketName {}, uuid {}", bucketName, uuid);
        }
        return null;
    }

}

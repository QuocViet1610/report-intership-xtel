package com.example.project1.utils;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.utils.OtherUtils.Utility;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
public class MinioUtils {
    private static String END_POINT;
    private static String ACCESS_KEY;
    private static String SECRET_KEY;

    @Value("${app.minio.local}")
    public void setEndPoint(String endPoint) {
        END_POINT = endPoint;
    }

    @Value("${app.minio.accessKey}")
    public void setAccessKey(String accessKey) {
        ACCESS_KEY = accessKey;
    }

    @Value("${app.minio.secretKey}")
    public void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    public static void uploadFile(String bucketName, String objectName, String path) {
        try {
            // minioClient = new MinioClient(END_POINT, ACCESS_KEY, SECRET_KEY);
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(END_POINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();
            // Check if the bucket already exists.

            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                System.out.println("Bucket NOT exists.");
                // Make a new bucket called asiatrip to hold a zip file of photos.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            Path filePath = Paths.get(path);

            try (InputStream is = Files.newInputStream(filePath)) {
                long size = Files.size(filePath);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(is, size, -1)
                            .build()
            );
        }

        } catch (Exception e) {
            log.error("Error: " + e);
        }

    }
    public static void deleteFileMinio(String bucketName, String objectName){
        try {
            // minioClient = new MinioClient(END_POINT, ACCESS_KEY, SECRET_KEY);
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(END_POINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();
            // Check if the bucket already exists.

            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                System.out.println("Bucket NOT exists.");
                // Make a new bucket called asiatrip to hold a zip file of photos.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

//            objectName = URLDecoder.decode(objectName, StandardCharsets.UTF_8.toString());

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
//            minioClient.removeObject(
//                    RemoveObjectArgs.builder()
//                            .bucket("sports")
//                            .object("category/1741169552184_1_(9).jpg")
//                            .build()
//            );

            System.out.println("Deleted " + objectName + " from bucket " + bucketName);
        } catch (Exception e) {
            log.error("Error: " + e);
        }

    }


    public static boolean doesFileExist(String bucketName, String fileName) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(END_POINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            return true;  // Nếu không lỗi, nghĩa là file tồn tại
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;  // File không tồn tại
            }
            throw new RuntimeException("Lỗi MinIO: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void upload(String bucketName, String objectName, String path, File file, String contentType) {
        try {
            // minioClient = new MinioClient(END_POINT, ACCESS_KEY, SECRET_KEY);
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(END_POINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();


            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                System.out.println("Bucket NOT exists.");
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            Path filePath = Paths.get(path);

            try (InputStream is = Files.newInputStream(filePath)) {
                long size = Files.size(filePath);

                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(is, size, -1)
                                .build()
                );
            }

        } catch (Exception e) {
            log.error("Error: " + e);
        }

    }

    public static String returnUrl(String bucketName, String objectName) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(END_POINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();

        return objectName;
    }

    public static String uploadToMinioAndGetUrl(MultipartFile file, String folderLocal, String bucketName, String keyName){
        if (!DataUtils.isNullOrEmpty(file)) {

            String format = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String imageName = DataUtils.convertNameToCode(StringUtils.cleanPath(Objects.requireNonNull(Utility.offsetDateTimeToEpochMilli(DateUtils.offSetDateNow()) + "_"  + file.getOriginalFilename()).toLowerCase()));

            if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                throw  new ValidateException(Translator.toMessage("error.common.file.length_max"));
            }
            File folder = new File(folderLocal);
            if(!folder.exists()){
                folder.mkdirs();
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw  new ValidateException(Translator.toMessage("error.common.file.not_image"));
            }

            Path fileNameAndPath = Paths.get(folderLocal, imageName);
            File outFile = new File(folderLocal + imageName);
            try {
                Files.write(fileNameAndPath, file.getBytes());
            } catch (IOException e) {
                log.error("error: " + e);
                throw new ValidateException(Translator.toMessage("error.common.file.save"));
            }

            if (contentType.equalsIgnoreCase("image/svg"))
                contentType = "image/svg+xml";

            MinioUtils.upload(bucketName, keyName + imageName, outFile.getPath(), outFile, "image/" + contentType);
            return MinioUtils.returnUrl(bucketName, keyName + imageName);
        }
        return null;
    }
}

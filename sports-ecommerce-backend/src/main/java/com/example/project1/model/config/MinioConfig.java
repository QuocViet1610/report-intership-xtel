package com.example.project1.model.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class MinioConfig {

    @Value("${app.minio.host}")
    private String minioHost;

    @Value("${app.minio.local}")
    private String minioHostLocal;

    @Value("${app.minio.accessKey}")
    private String minioAccessKey;

    @Value("${app.minio.secretKey}")
    private String minioSecretKey;

    @Value("${app.minio.bucketName}")
    private String minioBucketName;

    @Value("${app.image.folder.category}")
    private String minioCategoryFolder;

    @Value("${app.image.keyName.category}")
    private String minioCategoryKeyName;

    @Value("${app.image.folder.brand}")
    private String minioBrandFolder;

    @Value("${app.image.keyName.brand}")
    private String minioBrandKeyName;

    @Value("${app.image.folder.product}")
    private String minioProductFolder;

    @Value("${app.image.keyName.product}")
    private String minioProductKeyName;
}

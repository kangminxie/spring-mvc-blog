package com.kangmin.app.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String s3Region;

    @Value("${aws.s3.bucket}")
    private String s3Bucket;

    @Bean
    public AWSStaticCredentialsProvider awsStaticCredentialsProvider() {
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return new AWSStaticCredentialsProvider(basicAWSCredentials);
    }

    @Bean
    public AmazonS3 amazonS3Client(
        final AWSStaticCredentialsProvider awsStaticCredentialsProvider
    ) {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(awsStaticCredentialsProvider)
            .withRegion(getAwsRegion())
            .build();
    }

    @Bean
    public String s3BucketName() {
        // will be used in model => frontend
        return s3Bucket;
    }

    private Regions getAwsRegion() {
        return Regions.fromName(s3Region);
    }
}

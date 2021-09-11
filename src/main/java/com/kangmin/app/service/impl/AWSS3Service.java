package com.kangmin.app.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kangmin.app.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class AWSS3Service implements FileUploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3Service.class);

    private final AmazonS3 amazonS3;
    private final String s3BucketName;

    @Autowired
    public AWSS3Service(
        final AmazonS3 amazonS3,
        final String s3BucketName
    ) {
        this.amazonS3 = amazonS3;
        this.s3BucketName = s3BucketName;
    }

    @Override
    // @Async annotation ensures that the method is executed in a different background thread
    // but not consume the main thread.
    @Async
    public void uploadFile(final MultipartFile multipartFile) {
        LOGGER.info("File upload in progress.");
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            final String name = uploadFileToS3Bucket(s3BucketName, file);
            LOGGER.info("File upload is completed with filename: {}", name);
            final boolean deleteResult = file.delete();
            // To remove the file locally created in the project folder.
            LOGGER.info("File deletion result: {}:", deleteResult);
        } catch (final AmazonServiceException ex) {
            LOGGER.info("File upload is failed.");
            LOGGER.error("Error= {} while uploading file.", ex.getMessage());
        }
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            LOGGER.error("Error converting the multi-part file to file={}", ex.getMessage());
        }
        return file;
    }

    // for API usage
    private String uploadFileToS3Bucket(final String bucketName, final File file) {
        final String uniqueFileName = file.getName() + "_" + LocalDateTime.now();
        LOGGER.info("Uploading file with name= " + uniqueFileName);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
        amazonS3.putObject(putObjectRequest);
        return uniqueFileName;
    }

    @Override
    // @Async annotation ensures that the method is executed in a different background thread
    // but not consume the main thread.
    @Async
    public void uploadBlogImage(
        final String imageId,
        final MultipartFile multipartFile
    ) {
        LOGGER.info("File upload in progress.");
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            uploadBlogImageToS3Bucket(s3BucketName, imageId, file);
            LOGGER.info("File upload is completed.");
            final boolean deleteResult = file.delete();
            // To remove the file locally created in the project folder.
            LOGGER.info("File deletion result: {}:", deleteResult);
        } catch (final AmazonServiceException ex) {
            LOGGER.info("File upload is failed.");
            LOGGER.error("Error= {} while uploading file.", ex.getMessage());
        }
    }

    private void uploadBlogImageToS3Bucket(
        final String bucketName,
        final String imageId,
        final File file
    ) {
        LOGGER.info("Uploading file with imageId = " + imageId);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, imageId, file);
        amazonS3.putObject(putObjectRequest);
    }
}

package com.kangmin.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    void uploadFile(MultipartFile multipartFile);

    void uploadBlogImage(String imageUrl, MultipartFile blogImage);
}

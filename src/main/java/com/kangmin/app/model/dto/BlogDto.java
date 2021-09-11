package com.kangmin.app.model.dto;

import com.kangmin.app.model.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
public class BlogDto implements Serializable {

    private Long id;

    private String title;

    private String description;

    private String content;

    private Category category;

    private MultipartFile blogImage;

    // == for create & edit options
    private List<Category> categoryOptions;

    // == for edit ==
    private String imageId;
}

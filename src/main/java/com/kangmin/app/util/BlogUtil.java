package com.kangmin.app.util;

import com.kangmin.app.model.Account;
import com.kangmin.app.model.Blog;
import com.kangmin.app.model.Category;
import com.kangmin.app.model.dto.BlogDto;

public class BlogUtil {

    public static Blog convertToEntity(
        final BlogDto dto,
        final Account account,
        final Category category,
        final String imageId
    ) {
        return Blog.builder()
            .withTitle(dto.getTitle())
            .withDescription(dto.getDescription())
            .withContent(dto.getContent())
            .withAccount(account)
            .withCategory(category)
            .withImageId(imageId)
            .build();
    }

    public static BlogDto constructEditDao(final Blog blog) {
        final BlogDto dto = new BlogDto();
        dto.setId(blog.getId());
        dto.setTitle(blog.getTitle());
        dto.setDescription(blog.getDescription());
        dto.setContent(blog.getContent());
        dto.setCategory(blog.getCategory());
        dto.setImageId(blog.getImageId());
        return dto;
    }
}

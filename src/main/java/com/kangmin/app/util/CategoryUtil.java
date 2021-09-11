package com.kangmin.app.util;

import com.kangmin.app.model.Category;
import com.kangmin.app.model.dto.CategoryDto;

public class CategoryUtil {

    public static Category convertToEntity(final CategoryDto dto, final String accountId) {
        return new Category(dto.getName(), dto.getDescription(), accountId);
    }
}

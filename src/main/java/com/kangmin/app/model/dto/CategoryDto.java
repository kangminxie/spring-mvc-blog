package com.kangmin.app.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryDto implements Serializable {

    private Long id;

    private String name;

    private String description;

    public CategoryDto() {

    }

    public CategoryDto(
        final String name,
        final String description
    ) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category[id=" + id + ", name=" + name + ", description=" + description + "]";
    }
}

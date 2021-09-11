package com.kangmin.app.service;

import com.kangmin.app.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);

    List<Category> findAll();

    List<Category> findBySort(Sort sort);

    Page<Category> findPageableCategory(Pageable pageable);

    List<Category> findTop(int number);

    Category createOne(Category c);
}

package com.kangmin.app.service.impl;

import com.kangmin.app.dao.CategoryDao;
import com.kangmin.app.model.Category;
import com.kangmin.app.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    public CategoryServiceImpl(final CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public Optional<Category> findById(final Long id) {
        return categoryDao.findById(id);
    }

    @Override
    public Optional<Category> findByName(final String name) {
        return categoryDao.findByName(name);
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public List<Category> findBySort(final Sort sort) {
        return categoryDao.findAll(sort);
    }

    @Override
    public Page<Category> findPageableCategory(final Pageable pageable) {
        return categoryDao.findAll(pageable);
    }

    @Override
    public List<Category> findTop(final int number) {
        final Pageable pageable = getDefaultPageable(number);
        return categoryDao.findTop(pageable);
    }

    @Override
    public Category createOne(final Category c) {
        return categoryDao.save(c);
    }

    private Pageable getDefaultPageable(final int pageSize) {
        final Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return PageRequest.of(0, pageSize, sort);
    }
}

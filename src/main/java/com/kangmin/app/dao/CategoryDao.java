package com.kangmin.app.dao;

import com.kangmin.app.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryDao extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @Query("select c from Category c")
    List<Category> findTop(Pageable pageable);
}

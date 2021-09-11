package com.kangmin.app.service;

import com.kangmin.app.model.Account;
import com.kangmin.app.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BlogService {

    Page<Blog> findByPageable(Pageable pageable);

    Page<Blog> findByPageableWithQuery(Pageable pageable, String query);

    Page<Blog> findByPageableWithQueryAndCategoryId(Pageable pageable, String query, Long id);

    List<Blog> findAllWithCategoryId(Long id);

    Optional<Blog> getWithAccountIdAndId(String accountId, Long id);

    Optional<Blog> getWithUsernameAndId(String username, Long id);

    Optional<Blog> getAndConvert(Long id);

    Blog createOne(Blog blog);

    Blog update(Account account, Blog blog);
}

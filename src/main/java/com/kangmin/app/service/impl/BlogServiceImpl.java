package com.kangmin.app.service.impl;

import com.kangmin.app.dao.BlogDao;
import com.kangmin.app.model.Account;
import com.kangmin.app.model.Blog;
import com.kangmin.app.service.BlogService;
import com.kangmin.app.util.MarkdownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.kangmin.app.util.Constants.BLOG_PREVIEW_MAX_LENGTH;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogDao blogDao;

    public BlogServiceImpl(final BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    @Override
    public Page<Blog> findByPageable(final Pageable pageable) {
        // System.out.println(">>>> listBlog(pageable)");
        final Page<Blog> blogs = blogDao.findAll(pageable);
        return extendsDescription(blogs);
    }

    private Page<Blog> extendsDescription(final Page<Blog> blogs) {
        for (final Blog each : blogs) {
            final String description = each.getDescription();
            final String contentString = MarkdownUtils.markdownToText(each.getContent());
            each.setDescription(description + "<br/><br/><p>" + truncateAsNeed(contentString) + "....</p>");
        }
        return blogs;
    }

    @Override
    public Page<Blog> findByPageableWithQuery(
        final Pageable pageable,
        final String query
    ) {
        // System.out.println(">>>> listBlog(query, pageable)");
        if (query == null || query.isEmpty()) {
            return findByPageable(pageable);
        }

        final Page<Blog> blogs = blogDao.findByQuery(query, pageable);
        return extendsDescription(blogs);
    }

    @Override
    public Page<Blog> findByPageableWithQueryAndCategoryId(
        final Pageable pageable,
        final String query,
        final Long id
    ) {
        final Page<Blog> blogs;
        if (query == null || query.isEmpty()) {
            blogs = blogDao.findByCategoryId(id, pageable);
        } else {
            blogs = blogDao.findByQueryAndCategoryId(query, id, pageable);
        }
        return extendsDescription(blogs);
    }

    @Override
    public List<Blog> findAllWithCategoryId(final Long id) {
        final List<Blog> blogs = blogDao.findAllByCategoryId(id);
        for (final Blog each : blogs) {
            final String description = each.getDescription();
            final String contentString = MarkdownUtils.markdownToText(each.getContent());
            each.setDescription(description + "<br/><br/><p>" + truncateAsNeed(contentString) + "....</p>");
        }
        return blogs;
    }

    public Page<Blog> findPageableBlogWithStrictQuery(
        final Pageable pageable,
        final String titleQuery,
        final String categoryQuery
    ) {
        final Specification<Blog> specification = new Specification<Blog>() {
            @Override
            public Predicate toPredicate(
                final Root<Blog> root,
                final CriteriaQuery<?> cq,
                final CriteriaBuilder cb
            ) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.like(root.<String>get("title"), "%" + titleQuery + "%"));
                predicates.add(cb.like(root.<String>get("category").get("name"), "%" + categoryQuery + "%"));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        };

        final Page<Blog> blogs = blogDao.findAll(specification, pageable);
        return extendsDescription(blogs);
    }

    private String truncateAsNeed(final String raw) {
        if (raw == null || raw.length() < BLOG_PREVIEW_MAX_LENGTH) {
            return raw;
        }
        return raw.replace("#", "").substring(0, BLOG_PREVIEW_MAX_LENGTH) + "..";
    }

    @Override
    public Optional<Blog> getWithAccountIdAndId(final String accountId, final Long id) {
        return blogDao.findByAccountIdAndId(accountId, id);
    }

    @Override
    public Optional<Blog> getWithUsernameAndId(final String username, final Long id) {
        return blogDao.findByAccountUsernameAndId(username, id);
    }

    @Transactional
    @Override
    public Optional<Blog> getAndConvert(final Long id) {
        final Optional<Blog> blogOpt = blogDao.findById(id);
        if (blogOpt.isEmpty()) {
            return Optional.empty();
        }

        final Blog dbBlog = blogOpt.get();
        final Blog b = new Blog();
        // copyProperties(Object source, Object target)
        BeanUtils.copyProperties(dbBlog, b);
        final String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogDao.updateViews(id);
        return Optional.of(b);
    }

    @Override
    public Blog createOne(final Blog blog) {
        return blogDao.save(blog);
    }

    @Override
    public Blog update(final Account account, final Blog updateEntity) {
        final Optional<Blog> blogInDbOpt = blogDao.findByAccountIdAndId(account.getId(), updateEntity.getId());
        if (blogInDbOpt.isEmpty()) {
            throw new RuntimeException("You are not authorized to edit this blog");
        }

        final Blog blogInDb = blogInDbOpt.get();
        blogInDb.setTitle(updateEntity.getTitle());
        blogInDb.setDescription(updateEntity.getDescription());
        blogInDb.setContent(updateEntity.getContent());
        blogInDb.setCategory(updateEntity.getCategory());
        blogInDb.setImageId(updateEntity.getImageId());
        blogInDb.setUpdateTime(new Date());
        // == not need to update Account and Id ==
        return blogDao.save(blogInDb);
    }
}

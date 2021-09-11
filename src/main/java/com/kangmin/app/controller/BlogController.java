package com.kangmin.app.controller;

import com.kangmin.app.dao.AccountDao;
import com.kangmin.app.exception.WebResourceNotFoundException;
import com.kangmin.app.exception.WebUserUnauthorizedException;
import com.kangmin.app.model.Account;
import com.kangmin.app.model.Blog;
import com.kangmin.app.model.Category;
import com.kangmin.app.model.dto.BlogDto;
import com.kangmin.app.service.BlogService;
import com.kangmin.app.service.CategoryService;
import com.kangmin.app.service.FileUploadService;
import com.kangmin.app.util.BlogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static com.kangmin.app.util.CommonUtil.getFileExtension;
import static com.kangmin.app.util.Constants.ANONYMOUS_USERNAME;
import static com.kangmin.app.util.Constants.DEFAULT_BLOG_IMAGE_ID;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    private static final Logger LOG = LoggerFactory.getLogger(BlogController.class);

    private static final String BLOG_EMPTY_URL = "";
    private static final String BLOG_INDEX_URL = "/index";
    private static final String BLOG_HOME_URL = "/home";
    private static final int DEFAULT_HOME_PAGE_SIZE = 12;
    private static final int SHOW_CATEGORY_NUMBER = 12;

    private final AccountDao accountDao;
    private final BlogService blogService;
    private final CategoryService categoryService;
    private final FileUploadService awsS3Service;

    public BlogController(
        final AccountDao accountDao,
        final BlogService blogService,
        final CategoryService categoryService,
        final FileUploadService awsS3Service
    ) {
        this.accountDao = accountDao;
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.awsS3Service = awsS3Service;
    }

    // == blogs main & search page ==
    // == public access ==
    @RequestMapping({
        BLOG_EMPTY_URL,
        BLOG_HOME_URL,
        BLOG_INDEX_URL,
    })
    public String topBlogsPage(
        final @PageableDefault(
            size = DEFAULT_HOME_PAGE_SIZE,
            sort = {"updateTime"},
            direction = Sort.Direction.DESC
        ) Pageable pageable,
        final @RequestParam(value = "query", required = false) String query,
        final Model model
    ) {
        LOG.info("BlogController.topBlogsPage is visited with query={}", query);
        model.addAttribute("page", blogService.findByPageableWithQuery(pageable, query));
        model.addAttribute("categories", categoryService.findTop(SHOW_CATEGORY_NUMBER));
        model.addAttribute("query", query);
        model.addAttribute("viewByCategory", false);
        return "blog/blog-home";
    }

    // == individual blog page ==
    // == public access ==
    @GetMapping("/{id}")
    public String blogDetailPage(
        final @PathVariable String id,
        final Model model
    ) {
        LOG.debug("BlogController.blogDetailPage is visited with blog id={}", id);
        final long blogId;
        try {
            blogId = Long.parseLong(id);
        } catch (final NumberFormatException e) {
            throw new WebResourceNotFoundException("Please provide a valid blogId to view!");
        }

        final Optional<Blog> blogOpt = blogService.getAndConvert(blogId);
        if (blogOpt.isEmpty()) {
            throw new WebResourceNotFoundException(String.format("blog with id=%s does not exist!", id));
        }
        model.addAttribute("blog", blogOpt.get());
        return "blog/blog-view";
    }

    // == blogs based on category id ==
    // == public access ==
    @RequestMapping("/by-category/{id}")
    public String topBlogsInCategoryPage(
        final @PageableDefault(size = DEFAULT_HOME_PAGE_SIZE, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
        final @PathVariable Long id,
        final @RequestParam(value = "query", required = false) String query,
        final Model model
    ) {
        LOG.debug("BlogController.topBlogsInCategoryPage is visited with category id={} and query={}", id, query);
        model.addAttribute("page", blogService.findByPageableWithQueryAndCategoryId(pageable, query, id));
        model.addAttribute("categories", categoryService.findTop(SHOW_CATEGORY_NUMBER));
        model.addAttribute("query", query);
        model.addAttribute("viewByCategory", true);
        model.addAttribute("categoryId", id);
        return "blog/blog-home";
    }

    // == Create GET ==
    // == limited access ==
    @GetMapping("/create")
    public String showCreatePage(
        final Authentication auth,
        final Model model
    ) {
        LOG.debug("BlogController.showCreatePage is visited");
        if (auth == null || auth.getName() == null || auth.getName().equals(ANONYMOUS_USERNAME)) {
            throw new WebUserUnauthorizedException("You are not authorized to create a new blog!");
        }

        final BlogDto dto = new BlogDto();
        dto.setCategoryOptions(categoryService.findAll());
        model.addAttribute("dto", dto);
        return "blog/blog-create";
    }

    // == Create POST ==
    // == limited access ==
    @PostMapping("/create")
    public String processCreateRequest(
        final @ModelAttribute("dto") BlogDto dto,
        final Authentication auth
    ) {
        LOG.debug("BlogController.processCreateRequest is visited");
        final Account account = accountDao.findByUsername(auth.getName()).orElseThrow(
            () -> new WebUserUnauthorizedException("You are not authorized to create a new blog!")
        );

        final String imageId = handleBlogImageAndId(dto.getBlogImage(), DEFAULT_BLOG_IMAGE_ID);
        final Category category = handleInputCategory(dto.getCategory());

        final Blog blog = BlogUtil.convertToEntity(dto, account, category, imageId);
        blogService.createOne(blog);
        return "redirect:/blogs";
    }


    // == Edit GET ==
    // == limited access ==
    @GetMapping("/{id}/edit")
    public String showEditPage(
        final @PathVariable Long id,
        final Authentication auth,
        final Model model
    ) {
        LOG.debug("BlogController.showEditPage is visited");
        if (auth == null || auth.getName() == null || auth.getName().equals(ANONYMOUS_USERNAME)) {
            throw new WebUserUnauthorizedException("You are not authorized to edit this blog!");
        }

        final Optional<Blog> blogOpt = blogService.getWithUsernameAndId("sa", id);
        if (blogOpt.isEmpty()) {
            throw new WebUserUnauthorizedException("You are not authorized to edit this blog!");
        }

        final BlogDto dto = BlogUtil.constructEditDao(blogOpt.get());
        dto.setCategoryOptions(categoryService.findAll());
        model.addAttribute("dto", dto);
        return "blog/blog-edit";
    }

    // == Edit POST ==
    // == limited access ==
    @PostMapping("/edit")
    public String processEditRequest(
        final @ModelAttribute("dto") BlogDto dto,
        final Authentication auth
    ) {
        LOG.debug("BlogController.processEditRequest is visited");
        final Account account = accountDao.findByUsername(auth.getName()).orElseThrow(
            () -> new WebUserUnauthorizedException("You are not authorized to create a new blog!")
        );

        final Long blogId = dto.getId();
        final Optional<Blog> blogOpt = blogService.getWithAccountIdAndId(account.getId(), blogId);
        if (blogOpt.isEmpty()) {
            throw new WebUserUnauthorizedException("You are not authorized to edit this blog!");
        }

        final Blog blogInDb = blogOpt.get();
        final MultipartFile blogImage = dto.getBlogImage();
        final String imageId = handleBlogImageAndId(blogImage, blogInDb.getImageId());
        final Category category = handleInputCategory(dto.getCategory());
        final Blog blog = BlogUtil.convertToEntity(dto, account, category, imageId);
        blog.setId(blogId);

        blogService.update(account, blog);
        return "redirect:/blogs/" + blogId;
    }

    // == private help methods below ==
    // helper method to fetch the UserId of current loggedIn user
    private String getLoggedInUsername() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private String handleBlogImageAndId(final MultipartFile blogImage, final String defaultValue) {
        if (blogImage == null || blogImage.isEmpty()) {
            return defaultValue;
        }
        final String fileExtension = getFileExtension(blogImage.getOriginalFilename());
        final String uniqueImageId = UUID.randomUUID() + fileExtension;
        awsS3Service.uploadBlogImage(uniqueImageId, blogImage);
        return uniqueImageId;
    }

    private Category handleInputCategory(final Category dtoCategory) {
        return categoryService.findById(dtoCategory.getId()).orElse(
            categoryService.findByName("UnCategorized").orElseThrow(
                () -> new RuntimeException("Category Error!")
            )
        );
    }
}

package com.kangmin.app.controller;

import com.kangmin.app.dao.AccountDao;
import com.kangmin.app.exception.WebUserUnauthorizedException;
import com.kangmin.app.model.Account;
import com.kangmin.app.model.Blog;
import com.kangmin.app.model.Category;
import com.kangmin.app.model.dto.CategoryDto;
import com.kangmin.app.service.BlogService;
import com.kangmin.app.service.CategoryService;
import com.kangmin.app.util.CategoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kangmin.app.util.Constants.ANONYMOUS_USERNAME;

@Controller
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    private static final String CATEGORY_URL = "/category";

    private final AccountDao accountDao;
    private final BlogService blogService;
    private final CategoryService categoryService;

    public CategoryController(
        final AccountDao accountDao,
        final BlogService blogService,
        final CategoryService categoryService
    ) {
        this.accountDao = accountDao;
        this.blogService = blogService;
        this.categoryService = categoryService;
    }

    @RequestMapping({
        CATEGORY_URL,
    })
    public String categoryIndexPage(
        final @SortDefault(sort = {"id"}, direction = Sort.Direction.ASC) Sort sort,
        final Model model
    ) {
        LOG.debug("CategoryController.categoryIndexPage is visited");
        model.addAttribute("blogList", new ArrayList<Blog>());
        model.addAttribute("categories", categoryService.findBySort(sort));
        model.addAttribute("sortOptions", sort.toString());
        model.addAttribute("sortBy", sort.toString().split(":")[0]);
        return "category/category-home";
    }

    @RequestMapping(CATEGORY_URL + "/{id}")
    public String categorySpecificPage(
        final @PathVariable Long id,
        final @SortDefault(sort = {"id"}, direction = Sort.Direction.ASC) Sort sort,
        final Model model
    ) {
        LOG.debug("CategoryController.categorySpecificPage is visited with sort={}", sort.toString());
        model.addAttribute("blogList", blogService.findAllWithCategoryId(id));
        model.addAttribute("categories", categoryService.findBySort(sort));
        model.addAttribute("sortOptions", getSortOptions(sort));
        model.addAttribute("sortBy", sort.toString().split(":")[0]);
        return "category/category-home";
    }

    // == create new category GET ==
    // == limited access ==
    @GetMapping(CATEGORY_URL + "/create")
    public String showCreateCategoryPage(
        final Authentication auth,
        final Model model
    ) {
        LOG.debug("CategoryController.showCreatePage is visited");
        if (auth == null || auth.getName() == null || auth.getName().equals(ANONYMOUS_USERNAME)) {
            throw new WebUserUnauthorizedException("You are not authorized to create a new category!");
        }

        final CategoryDto dto = new CategoryDto();
        model.addAttribute("dto", dto);
        return "category/category-create";
    }

    // == post ==
    @PostMapping(CATEGORY_URL + "/create")
    public String processCreateCategoryRequest(
        final Authentication auth,
        final @ModelAttribute("dto") CategoryDto dto
    ) {
        LOG.debug("processCreateCategoryRequest for CategoryDto dto =" + dto);
        final Optional<Account> loggedInAccount = accountDao.findByUsername(auth.getName());
        if (loggedInAccount.isEmpty()) {
            throw new WebUserUnauthorizedException("Please login in to perform any action");
        }
        final String accountId = loggedInAccount.get().getId();
        final Category categoryToCreate = CategoryUtil.convertToEntity(dto, accountId);
        categoryService.createOne(categoryToCreate);
        return "redirect:/category/";
    }

    private List<String> getSortOptions(final Sort sort) {
        return sort.stream()
            .map(Sort.Order::toString)
            .collect(Collectors.toList());
    }
}

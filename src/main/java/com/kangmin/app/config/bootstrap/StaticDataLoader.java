package com.kangmin.app.config.bootstrap;

import com.kangmin.app.config.EnvConfig;
import com.kangmin.app.config.security.PasswordConfig;
import com.kangmin.app.dao.AccountDao;
import com.kangmin.app.dao.BlogDao;
import com.kangmin.app.dao.CategoryDao;
import com.kangmin.app.dao.CommentDao;
import com.kangmin.app.model.Account;
import com.kangmin.app.model.Blog;
import com.kangmin.app.model.Category;
import com.kangmin.app.model.Comment;
import com.kangmin.app.model.profile.EnvNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.kangmin.app.util.Constants.DEFAULT_BLOG_IMAGE_ID;
import static com.kangmin.app.util.Constants.DEFAULT_BLOG_IMAGE_ID_2;
import static com.kangmin.app.util.Constants.DEFAULT_BLOG_IMAGE_ID_3;

@Import({
    EnvConfig.class,
    PasswordConfig.class,
})
@Component
public class StaticDataLoader implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(StaticDataLoader.class);

    private final EnvNode envNode;
    private final AccountDao accountDao;
    private final BlogDao blogDao;
    private final CommentDao commentDao;
    private final CategoryDao categoryDao;
    private final PasswordEncoder passwordEncoder;

    public StaticDataLoader(
        final EnvNode envNode,
        final AccountDao accountDao,
        final BlogDao blogDao,
        final CommentDao commentDao,
        final CategoryDao categoryDao,
        final PasswordEncoder passwordEncoder
    ) {
        this.envNode = envNode;
        this.accountDao = accountDao;
        this.blogDao = blogDao;
        this.commentDao = commentDao;
        this.categoryDao = categoryDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(final String... args) {
        if (accountDao.findAll().isEmpty()) {
            final List<Account> initAccounts = createInitAccounts();
            accountDao.saveAll(initAccounts);
            if (blogDao.findAll().isEmpty()) {
                createInitBlogs();
            }
        } else if (envNode == EnvNode.PROD) {
            LOG.info(">>> Application in PROD mode is up >>>");
        }
    }

    private List<Account> createInitAccounts() {
        // developer, also super admin
        final Account dev = Account.builder()
            .withId("uuid-0000")
            .withEmail("dev@security.com")
            .withUsername("dev")
            .withName("Name-dev")
            .withRole("SUPER_ADMIN")
            .withPassword(passwordEncoder.encode("dev"))
            .build();

        // super admin, h2-console one
        final Account sa = Account.builder()
            .withId("uuid-0001")
            .withEmail("sa@sa.com")
            .withUsername("sa")
            .withName("MountKing Sa")
            .withRole("SUPER_ADMIN")
            .withPassword(passwordEncoder.encode("password"))
            .build();

        // normal
        final Account normal = Account.builder()
            .withId("uuid-0111")
            .withEmail("normal111@security.com")
            .withUsername("normal")
            .withName("Name-normal")
            .withRole("NORMAL")
            .withPassword(passwordEncoder.encode("normal"))
            .build();

        // regular admin
        final Account admin = Account.builder()
            .withId("uuid-0222")
            .withEmail("admin222@security.com")
            .withUsername("admin")
            .withName("Name-admin")
            .withRole("ADMIN")
            .withPassword(passwordEncoder.encode("admin"))
            .build();

        return Arrays.asList(dev, sa, normal, admin);
    }

    private void createInitBlogs() {
        final Account sa = accountDao.findByUsername("sa").orElseThrow(() -> {
            throw new RuntimeException("sa is not exist");
        });

        final Category c0 = new Category("UnCategorized", "Unspecified Category", sa.getId());
        categoryDao.save(c0);

        final Category c1 = new Category("DEV-Learning", "Long long way to go", sa.getId());
        categoryDao.save(c1);
        final Blog b1 = Blog.builder()
            .withTitle("DEV-The last blog in 2020")
            .withDescription("This is a hard year for learning, right?")
            .withContent("<p>The best way to learn is learn to learn"
                + "<h2>The truth is that</h2>，we usually care too much about the outcome."
                + "We shall enjoy the process, and the chance we could learn</p>")
            .withAccount(sa)
            .withCategory(c1)
            .withImageId(DEFAULT_BLOG_IMAGE_ID_3)
            .build();
        blogDao.save(b1);

        final Category c2 = new Category("DEV-Thinking", "record your thoughts", sa.getId());
        categoryDao.save(c2);
        final Blog b2 = Blog.builder()
            .withTitle("DEV-The first blog in 2021")
            .withDescription("This is a new year, right?")
            .withContent("<p>We are still fighting against the COVID-19. "
                + "Different people have different ideas of how we shall deal with it. "
                + "Protect yourself and your loved ones, believe we could get through")
            .withAccount(sa)
            .withCategory(c2)
            .withImageId(DEFAULT_BLOG_IMAGE_ID_2)
            .build();
        blogDao.save(b2);

        final Blog b3 = Blog.builder()
            .withTitle("DEV-Spring and Spring Cloud is cool 2021")
            .withDescription("Tech is good, especially the cloud")
            .withContent("# To further your journey in spring world, "
                + "maybe we can try spring cloud the next. "
                + "Of course, we can get started with SpringFlux.")
            .withAccount(sa)
            .withCategory(c1)
            .withImageId(DEFAULT_BLOG_IMAGE_ID)
            .build();
        blogDao.save(b3);

        final Comment cm1 = new Comment("I am comment cm1", sa.getId());
        cm1.setBlog(b3);
        commentDao.save(cm1);

        final Comment cm2 = new Comment("This is comment cm2", sa.getId());
        cm2.setBlog(b3);
        commentDao.save(cm2);

        final Category c3 = new Category("DEV-Algorithms", "Coding for Algorithms", sa.getId());
        categoryDao.save(c3);

        final Category c4 = new Category("DEV-Frontend", "Such as React, Redux", sa.getId());
        categoryDao.save(c4);

        final Category c5 = new Category("DEV-Spring", "Spring Core: MVC, AOP", sa.getId());
        categoryDao.save(c5);
    }
}

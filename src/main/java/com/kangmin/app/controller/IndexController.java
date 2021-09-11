package com.kangmin.app.controller;

import com.kangmin.app.model.profile.EnvNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.kangmin.app.util.Constants.ANONYMOUS_USERNAME;

@Controller
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    private static final String INDEX_URL = "/";
    private static final String EMPTY_URL = "";
    private static final String HOME_URL = "/home";
    private static final String BLOG_HOME_URL = "/blogs/**";
    private static final String CATEGORY_URL = "/category/**";
    private static final String ABOUT_URL = "/about";
    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_URL = "/register";

    private final EnvNode envNode;

    @Autowired
    public IndexController(final EnvNode envNode) {
        this.envNode = envNode;
    }

    @RequestMapping({
        INDEX_URL,
        EMPTY_URL,
        BLOG_HOME_URL,
        CATEGORY_URL,
    })
    public String showIndexPage() {
        LOG.debug("IndexController.showIndexPage is visited with envNode=" + envNode);
        return "index";
    }

    @RequestMapping(ABOUT_URL)
    public String showAboutPage() {
        return "index";
    }

    @RequestMapping(LOGIN_URL)
    public String showLoginPage() {
        // == check if user already logged-in ==
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName() == null
            || auth.getName().equals(ANONYMOUS_USERNAME)
            || auth instanceof AnonymousAuthenticationToken) {
            // == not logged-in ==
            return "login";
        }

        // == already logged-in ==
        return "redirect:/blogs";
    }

    @RequestMapping(REGISTER_URL)
    public String showRegisterPage() {
        return "index";
    }

    @RequestMapping(HOME_URL)
    public String redirectToBlogHome() {
        return "redirect:/blogs/home";
    }

    // == IndexController wide model ==
    @ModelAttribute("envNode")
    public String getCurrentEnvNode() {
        return this.envNode.toString();
    }
}

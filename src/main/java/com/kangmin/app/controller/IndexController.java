package com.kangmin.app.controller;

import com.kangmin.app.model.profile.EnvNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    private final EnvNode envNode;

    @Autowired
    public IndexController(final EnvNode envNode) {
        this.envNode = envNode;
    }

    @RequestMapping("")
    public String viewIndexPage() {
        return "index";
    }

    @RequestMapping("/health")
    @ResponseBody
    public String checkHealth() {
        return "App is OK at timestamp=" + System.currentTimeMillis();
    }

    // == IndexController wide model ==
    @ModelAttribute("envNode")
    public String getCurrentEnvNode() {
        return this.envNode.toString();
    }
}

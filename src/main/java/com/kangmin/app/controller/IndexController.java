package com.kangmin.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @RequestMapping("")
    @ResponseBody
    public String checkHealth() {
        return "App is OK for 2021-08-28_17:38";
    }
}

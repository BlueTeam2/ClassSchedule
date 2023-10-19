package com.softserve.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@Api(tags = "FrontEnd API")
public class FrontendController {

    @RequestMapping(path = {"/", "/login", "/admin", "/schedule", "/activation-page"})
    public String staticResource() {
        return "index";
    }

}

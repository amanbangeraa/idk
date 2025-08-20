package com.employeems.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home controller for basic navigation
 */
@Controller
public class HomeController {
    
    /**
     * GET / - Home page
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }
}



package com.wuzzuf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingAnalyzer {
    @GetMapping("/")
    public String homepage(Model model) {
        return "analyzer_home";
    }
}

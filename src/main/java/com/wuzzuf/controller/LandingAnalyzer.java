package com.wuzzuf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.wuzzuf.service.WuzzufDataFrame;
import joinery.DataFrame;
@Controller
public class LandingAnalyzer {
    @GetMapping("/")
    public String homepage(Model model) {
        WuzzufDataFrame wuzzuf_df=new WuzzufDataFrame();
        DataFrame df =wuzzuf_df.prepareDataFrame();
        System.out.println(df.length());
        return "analyzer_home";
    }
}

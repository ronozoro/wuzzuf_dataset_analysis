package com.wuzzuf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.wuzzuf.model.WuzzufDataFrame;
import smile.data.DataFrame;
import static com.wuzzuf.utils.Annotation.*;
@Controller
public class LandingAnalyzer {
    @GetMapping("/")
    public String homepage(Model model) {
        WuzzufDataFrame wuzzuf_df=new WuzzufDataFrame();
        DataFrame df =wuzzuf_df.prepareDataFrame(KAGGLE_Path_DOWNLOAD,"dataset.csv");

        System.out.println();
        return "analyzer_home";
    }
}

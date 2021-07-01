package com.wuzzuf.controller;

import com.wuzzuf.model.WuzzufDataFrame;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import smile.data.DataFrame;
import smile.data.Tuple;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.wuzzuf.utils.Annotation.KAGGLE_Path_DOWNLOAD;

@Controller
public class MainController {
    @GetMapping("/")
    public String homepage(Model model) throws IOException {
        WuzzufDataFrame wuzzuf_df = new WuzzufDataFrame();
        String db_name = "dataset";
        wuzzuf_df.prepareDataFrame(KAGGLE_Path_DOWNLOAD, db_name);
        DataFrame df = wuzzuf_df.getWuzzufJobs();
        List<Tuple> df_limited = df.stream().limit(5).toList();
        DataFrame cleaned_df = wuzzuf_df.cleanData(df);
        List<Map.Entry<String, Integer>> jobsByCompany = wuzzuf_df.jobsByCompany(cleaned_df);
        List<Map.Entry<String, Integer>> CountJob = wuzzuf_df.JobCounter(cleaned_df);
        List<Map.Entry<String, Integer>> job_by_area = wuzzuf_df.JobByArea(cleaned_df);
        List<Map.Entry<String, Integer>> skills_counter = wuzzuf_df.SkillsCount(cleaned_df);
        DataFrame factorized_df =wuzzuf_df.FactorizeData(cleaned_df);
        String img_base64=wuzzuf_df.KmeanGraph(cleaned_df);

        model.addAttribute("df_limited", df_limited);
        model.addAttribute("df_cleaned", cleaned_df);
        model.addAttribute("job_by_company", jobsByCompany);
        model.addAttribute("job_counter", CountJob);
        model.addAttribute("job_by_area", job_by_area);
        model.addAttribute("skills_counter", skills_counter);
        model.addAttribute("factorized_df", factorized_df.stream().limit(5).toList());
        model.addAttribute("imag_src", img_base64);
        model.addAttribute("df", df);
        return "analyzer_home";
    }
}

package com.wuzzuf.controller;

import com.wuzzuf.dao.DataFrameDaoImp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smile.data.DataFrame;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.wuzzuf.utils.Annotation.KAGGLE_Path_DOWNLOAD;

@RestController
public class RestApi {
    String db_name = "dataset";

    @GetMapping(value = "/api/skill")
    public List<Map.Entry<String, Integer>> getDataframe(@RequestParam(value = "name", defaultValue = "Null") String skill) throws IOException, URISyntaxException {
        DataFrameDaoImp wuzzuf_df = new DataFrameDaoImp(KAGGLE_Path_DOWNLOAD, db_name);
        DataFrame df = wuzzuf_df.getWuzzufDataFrame();
        DataFrame cleaned_df = wuzzuf_df.cleanData(df);
        if (!skill.equals("Null")) {
            return wuzzuf_df.SkillsCount(cleaned_df).stream().filter(row -> row.getKey().toLowerCase(Locale.ROOT).equals(skill.toLowerCase(Locale.ROOT))).toList();
        } else {
            return wuzzuf_df.SkillsCount(cleaned_df);
        }
    }

    @GetMapping(value = "/api/job/company")
    public List<Map.Entry<String, Integer>> getJobsByCompany(@RequestParam(value = "name", defaultValue = "Null") String company_name) throws IOException, URISyntaxException {

        DataFrameDaoImp wuzzuf_df = new DataFrameDaoImp(KAGGLE_Path_DOWNLOAD, db_name);
        DataFrame df = wuzzuf_df.getWuzzufDataFrame();
        DataFrame cleaned_df = wuzzuf_df.cleanData(df);

        if (!company_name.equals("Null")) {
            return wuzzuf_df.jobsByCompany(cleaned_df).stream().filter(row -> row.getKey().toLowerCase(Locale.ROOT).equals(company_name.toLowerCase(Locale.ROOT))).toList();
        } else {
            return wuzzuf_df.jobsByCompany(cleaned_df);
        }
    }

    @GetMapping(value = "/api/job/count")
    public List<Map.Entry<String, Integer>> JobsCount(@RequestParam(value = "name", defaultValue = "Null") String job_name) throws IOException, URISyntaxException {
        DataFrameDaoImp wuzzuf_df = new DataFrameDaoImp(KAGGLE_Path_DOWNLOAD, db_name);
        DataFrame df = wuzzuf_df.getWuzzufDataFrame();
        DataFrame cleaned_df = wuzzuf_df.cleanData(df);
        if (!job_name.equals("Null")) {
            return wuzzuf_df.JobCounter(cleaned_df).stream().filter(row -> row.getKey().toLowerCase(Locale.ROOT).equals(job_name.toLowerCase(Locale.ROOT))).toList();
        } else {
            return wuzzuf_df.JobCounter(cleaned_df);
        }
    }

    @GetMapping(value = "/api/job/area")
    public List<Map.Entry<String, Integer>> JobsByLocation(@RequestParam(value = "name", defaultValue = "Null") String area) throws IOException, URISyntaxException {
        DataFrameDaoImp wuzzuf_df = new DataFrameDaoImp(KAGGLE_Path_DOWNLOAD, db_name);
        DataFrame df = wuzzuf_df.getWuzzufDataFrame();
        DataFrame cleaned_df = wuzzuf_df.cleanData(df);
        if (!area.equals("Null")) {
            return wuzzuf_df.JobByArea(cleaned_df).stream().filter(row -> row.getKey().toLowerCase(Locale.ROOT).equals(area.toLowerCase(Locale.ROOT))).toList();
        } else {
            return wuzzuf_df.JobByArea(cleaned_df);
        }
    }
}

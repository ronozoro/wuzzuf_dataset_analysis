package com.wuzzuf.service;

import com.wuzzuf.model.WuzzufJobs;
import com.wuzzuf.utils.LoadDataset;
import joinery.DataFrame;

import java.util.Arrays;
import java.util.List;

public class WuzzufDataFrame {

    public DataFrame prepareDataFrame() {
        LoadDataset ds = new LoadDataset();
        List<WuzzufJobs> jobs = ds.get_dataset_list("dataset");
        DataFrame<String> df = new DataFrame<>("title", "company", "location", "type", "level", "yrs_exp", "country", "skills");
        for (WuzzufJobs job : jobs) {
            String title = job.getTitle();
            String company = job.getCompany();
            String location = job.getLocation();
            String type = job.getType();
            String level = job.getLevel();
            String yrs_exp = job.getExpYears();
            String country = job.getCountry();
            String skills = job.getSkills();
            df.append(Arrays.asList(title, company, location, type, level, yrs_exp, country, skills));
        }
        return df;
    }

}

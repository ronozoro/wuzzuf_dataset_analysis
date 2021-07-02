package com.wuzzuf.dao;
import smile.data.DataFrame;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DataFrameDao {
    public List<Map.Entry<String, Integer>> SkillsCount(DataFrame df);
    public List<Map.Entry<String, Integer>> jobsByCompany(DataFrame df);
    public List<Map.Entry<String, Integer>> JobCounter(DataFrame df);
    public List<Map.Entry<String, Integer>> JobByArea(DataFrame df);
    public String KmeanGraph(DataFrame df) throws IOException;
    public DataFrame cleanData(DataFrame df);
    public DataFrame FactorizeData(DataFrame df);
    public List<Map.Entry<String, Integer>> prepare_all_data(String[] col_names, String col_group_by, DataFrame df);
    public DataFrame getWuzzufDataFrame();

}

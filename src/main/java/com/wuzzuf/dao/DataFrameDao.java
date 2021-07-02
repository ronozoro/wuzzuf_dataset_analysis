package com.wuzzuf.dao;
import smile.data.DataFrame;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DataFrameDao {
    List<Map.Entry<String, Integer>> SkillsCount(DataFrame df);
    List<Map.Entry<String, Integer>> jobsByCompany(DataFrame df);
    List<Map.Entry<String, Integer>> JobCounter(DataFrame df);
    List<Map.Entry<String, Integer>> JobByArea(DataFrame df);
    String KmeanGraph(DataFrame df) throws IOException;
    DataFrame cleanData(DataFrame df);
    DataFrame FactorizeData(DataFrame df);
    List<Map.Entry<String, Integer>> prepare_all_data(String[] col_names, String col_group_by, DataFrame df);
    DataFrame getWuzzufDataFrame();

}

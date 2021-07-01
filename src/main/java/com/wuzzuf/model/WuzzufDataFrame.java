package com.wuzzuf.model;

import com.wuzzuf.utils.LoadDataset;
import org.apache.commons.csv.CSVFormat;
import smile.clustering.KMeans;
import smile.clustering.PartitionClustering;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.measure.NominalScale;
import smile.data.vector.IntVector;
import smile.io.Read;
import smile.plot.swing.ScatterPlot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;


public class WuzzufDataFrame {
    private DataFrame WuzzufJobs;

    private static int[] factorizeYears(DataFrame df, String col_name) {
        String[] values = df.stringVector(col_name).distinct().toArray(new String[]{});
        return df.stringVector(col_name).factorize(new NominalScale(values)).toIntArray();
    }

    public  List<Map.Entry<String, Integer>> SkillsCount(DataFrame df) {
        df = df.select("Skills");
        HashMap<String, Integer> word_count_map = new HashMap();
        for (Tuple xx : df.stream().toList()) {
            String[] words = xx.get(0).toString().split(",");
            for (String word : words) {
                int freq = word_count_map.getOrDefault(word, 0);
                word_count_map.put(word, ++freq);
            }

        }

        return word_count_map.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue())).toList();
    }

    public List<Map.Entry<String, Integer>> jobsByCompany(DataFrame df) {
        String[] company_filter = {"Company", "Title"};
        WuzzufDataFrame df_current = new WuzzufDataFrame();
        return df_current.prepare_all_data(company_filter, "Company", df);
    }

    public List<Map.Entry<String, Integer>> JobCounter(DataFrame df) {
        WuzzufDataFrame df_current = new WuzzufDataFrame();
        String[] job_filter = {"Title"};
        return df_current.prepare_all_data(job_filter, "Title", df);
    }

    public List<Map.Entry<String, Integer>> JobByArea(DataFrame df) {
        WuzzufDataFrame df_current = new WuzzufDataFrame();
        String[] area_filter = {"Location", "Country"};
        return df_current.prepare_all_data(area_filter, "Location", df);
    }

    public String KmeanGraph(DataFrame df) throws IOException {
        WuzzufDataFrame df_current = new WuzzufDataFrame();
        df = df_current.FactorizeData(df);
        DataFrame kmean = df.select("CompanyFact", "JobsFact");
        KMeans clusters = PartitionClustering.run(100, () -> KMeans.fit(kmean.toArray(), 4));
        BufferedImage image = ScatterPlot.of(kmean.toArray(), clusters.y, '.').canvas().setAxisLabels("Companies", "Jobs").toBufferedImage(900, 500);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        return Base64.getEncoder().encodeToString(output.toByteArray());
    }

    public void prepareDataFrame(String path, String dataset) {
        CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(',');
        LoadDataset loader = new LoadDataset();
        loader.download_csv(dataset);
        DataFrame df = null;
        try {
            df = Read.csv(path + dataset + ".csv", format);
            df.select("Title", "Company", "Location", "Type", "Level", "YearsExp", "Country", "Skills");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        WuzzufJobs = df;
    }

    public DataFrame cleanData(DataFrame df) {
        df = df.omitNullRows();
        df = DataFrame.of(df.stream().filter(row -> !row.getString("YearsExp").contains("null")));
        df = DataFrame.of(df.stream().filter(row -> !row.getString("Skills").contains("null")));
        df = DataFrame.of(df.stream().distinct());
        return df;
    }

    public DataFrame FactorizeData(DataFrame df) {
        df = df.merge(IntVector.of("YearsExpFact", factorizeYears(df, "YearsExp")));
        df = df.merge(IntVector.of("JobsFact", factorizeYears(df, "Title")));
        df = df.merge(IntVector.of("CompanyFact", factorizeYears(df, "Company")));
        return df;
    }


    private List<Map.Entry<String, Integer>> prepare_all_data(String[] col_names, String col_group_by, DataFrame df) {
        DataFrame df_jobs = df.select(col_names);
        Map<String, List<Tuple>> grouped_by_col = df_jobs.stream().collect(Collectors.groupingBy(row -> row.getString(col_group_by)));
        HashMap<String, Integer> col_map = new HashMap();
        for (Map.Entry<String, List<Tuple>> entry : grouped_by_col.entrySet()) {
            col_map.put(entry.getKey(), entry.getValue().size());
        }
        return col_map.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue())).toList();
    }

    private  List<Map.Entry<String, Integer>> prepare_data_filtered(String[] col_names, String col_group_by, DataFrame df, int min, int max) {
        DataFrame df_jobs = df.select(col_names);
        Map<String, List<Tuple>> grouped_by_col = df_jobs.stream().collect(Collectors.groupingBy(row -> row.getString(col_group_by)));
        HashMap<String, Integer> col_map = new HashMap();
        for (Map.Entry<String, List<Tuple>> entry : grouped_by_col.entrySet()) {
            col_map.put(entry.getKey(), entry.getValue().size());
        }
        return col_map.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue())).filter(row -> row.getValue() >= min && row.getValue() <= max).toList();
    }


    public DataFrame getWuzzufJobs() {
        return WuzzufJobs;
    }
}

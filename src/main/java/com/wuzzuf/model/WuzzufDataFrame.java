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

    public static int[] factorizeYears(DataFrame df, String col_name) {
        String[] values = df.stringVector(col_name).distinct().toArray(new String[]{});
        return df.stringVector(col_name).factorize(new NominalScale(values)).toIntArray();
    }

    public DataFrame prepareDataFrame(String path, String dataset) {
        CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(',');
        LoadDataset loader = new LoadDataset();
        WuzzufDataFrame df_current = new WuzzufDataFrame();
        List<WuzzufJobs> jobs = loader.download_csv(dataset);
        DataFrame df = null;
        try {
            df = Read.csv(path + dataset, format);
            df.select("Title", "Company", "Location", "Type", "Level", "YearsExp", "Country", "Skills");
            df = df.omitNullRows();
            df = DataFrame.of(df.stream().filter(row -> !row.getString("YearsExp").contains("null")));
            df = DataFrame.of(df.stream().filter(row -> !row.getString("Skills").contains("null")));
            df = df.merge(IntVector.of("YearsExpFact", factorizeYears(df, "YearsExp")));
            df = df.merge(IntVector.of("CompanyFact", factorizeYears(df, "Company")));
            df = df.merge(IntVector.of("JobsFact", factorizeYears(df, "Title")));
            df = DataFrame.of(df.stream().distinct());

            String[] company_filter = {"Company", "Title"};
            Object[] company_array = df_current.prepare_all_data(company_filter, "Company", df);
            Object[] company_filtered = df_current.prepare_data_filtered(company_filter, "Company", df, 10, 20);
            for (Object item : company_array) {
                System.out.println("Company : " + ((Map.Entry<String, Integer>) item).getKey() + " , Jobs Count : " + ((Map.Entry<String, Integer>) item).getValue());
            }

            for (Object item : company_filtered) {
                System.out.println("Company : " + ((Map.Entry<String, Integer>) item).getKey() + " , Jobs Count: " + ((Map.Entry<String, Integer>) item).getValue());
            }


            String[] job_filter = {"Title"};
            Object[] job_array = df_current.prepare_all_data(job_filter, "Title", df);
            Object[] job_filtered = df_current.prepare_data_filtered(job_filter, "Title", df, 5, 60);
            for (Object item : job_array) {
                System.out.println("Job : " + ((Map.Entry<String, Integer>) item).getKey() + " , Count : " + ((Map.Entry<String, Integer>) item).getValue());
            }

            for (Object item : job_filtered) {
                System.out.println("Job : " + ((Map.Entry<String, Integer>) item).getKey() + " , Count : " + ((Map.Entry<String, Integer>) item).getValue());
            }


            String[] area_filter = {"Location", "Country"};
            Object[] location_array = df_current.prepare_all_data(area_filter, "Location", df);
            Object[] location_filtered = df_current.prepare_data_filtered(area_filter, "Location", df, 20, 600);
            for (Object item : location_array) {
                System.out.println("Location : " + ((Map.Entry<String, Integer>) item).getKey() + " , Job Count : " + ((Map.Entry<String, Integer>) item).getValue());
            }

            for (Object item : location_filtered) {
                System.out.println("Location : " + ((Map.Entry<String, Integer>) item).getKey() + " , Job Count : " + ((Map.Entry<String, Integer>) item).getValue());
            }

            DataFrame kmean = df.select("CompanyFact", "JobsFact");
            KMeans clusters = PartitionClustering.run(100, () -> KMeans.fit(kmean.toArray(), 4));
            BufferedImage image = ScatterPlot.of(kmean.toArray(), clusters.y, '.').canvas().setAxisLabels("Companies", "Jobs").toBufferedImage(800, 800);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(image, "png", output);
            String imageAsBase64 = Base64.getEncoder().encodeToString(output.toByteArray());

            System.out.println(imageAsBase64);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        WuzzufJobs = df;
        return df;
    }

    private Object[] prepare_all_data(String[] col_names, String col_group_by, DataFrame df) {
        DataFrame df_jobs = df.select(col_names);
        Map<String, List<Tuple>> grouped_by_col = df_jobs.stream().collect(Collectors.groupingBy(row -> row.getString(col_group_by)));
        HashMap<String, Integer> col_map = new HashMap();
        for (Map.Entry<String, List<Tuple>> entry : grouped_by_col.entrySet()) {
            col_map.put(entry.getKey(), entry.getValue().size());
        }
        Object[] col_array = col_map.entrySet().toArray();
        Arrays.sort(col_array, (Comparator) (o1, o2) -> ((Map.Entry<String, Integer>) o2).getValue().compareTo(((Map.Entry<String, Integer>) o1).getValue()));
        return col_array;
    }

    private Object[] prepare_data_filtered(String[] col_names, String col_group_by, DataFrame df, int min, int max) {
        DataFrame df_jobs = df.select(col_names);
        Map<String, List<Tuple>> grouped_by_col = df_jobs.stream().collect(Collectors.groupingBy(row -> row.getString(col_group_by)));
        HashMap<String, Integer> col_map = new HashMap();
        for (Map.Entry<String, List<Tuple>> entry : grouped_by_col.entrySet()) {
            col_map.put(entry.getKey(), entry.getValue().size());
        }
        Object[] col_array = col_map.entrySet().toArray();
        Arrays.sort(col_array, (Comparator) (o1, o2) -> ((Map.Entry<String, Integer>) o2).getValue().compareTo(((Map.Entry<String, Integer>) o1).getValue()));
        Object[] col_filtered = Arrays.stream(col_array).filter(row -> ((Map.Entry<String, Integer>) row).getValue() >= min && ((Map.Entry<String, Integer>) row).getValue() <= max).toArray();
        return col_filtered;
    }


    public DataFrame getWuzzufJobs() {
        return WuzzufJobs;
    }

    public static void main(String[] args) {
        WuzzufDataFrame dq = new WuzzufDataFrame();
        dq.prepareDataFrame("../wuzzuf_dataset_analysis/src/main/resources/datasets/", "dataset.csv");

    }


}

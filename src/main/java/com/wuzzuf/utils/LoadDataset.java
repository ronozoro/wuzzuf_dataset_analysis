package com.wuzzuf.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.wuzzuf.model.WuzzufJobs;
import static com.wuzzuf.utils.Annotation.*;

public class LoadDataset {

    public List<WuzzufJobs> get_dataset_list(String dataset_name) {
        LoadDataset ds = new LoadDataset();
        return ds.download_csv(dataset_name);
    }
    public List<WuzzufJobs> download_csv(String dataset_name) {
        String dataset_path = KAGGLE_API_BASE_URL;
        String[] row_data;
        List<WuzzufJobs> jobs = new ArrayList<>();
        try {
            URL direct = new URL(dataset_path);
            HttpURLConnection urlConnection = null;
            urlConnection = (HttpURLConnection) direct.openConnection();
            urlConnection.setRequestMethod("GET");
            String auth = defaultKName + ":" + defaultKPass;
            byte[] encodeAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.ISO_8859_1)
            );
            String authHeaderValue = "Basic " + new String(encodeAuth);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            urlConnection.setRequestProperty("Authorization", authHeaderValue);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();
            BufferedReader bufReader = new BufferedReader(new StringReader(response.toString()));
            String line = null;
            FileWriter file = new FileWriter(KAGGLE_Path_DOWNLOAD +dataset_name+ ".csv");
            PrintWriter write = new PrintWriter(file);
            int i=0;
            while ((line = bufReader.readLine()) != null) {
                row_data = line.split(",");
                if(i>0){
                    WuzzufJobs job = new WuzzufJobs(row_data[0],row_data[1],row_data[2],row_data[3],row_data[4],row_data[5],row_data[6],row_data[7]);
                    jobs.add(job);
                }
                write.println(line);
                i++;
            }
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jobs;
    }


}

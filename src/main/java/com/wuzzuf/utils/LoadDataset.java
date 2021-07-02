package com.wuzzuf.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.wuzzuf.utils.Annotation.*;

public class LoadDataset {

    public void download_csv(String dataset_name) {
        String dataset_path = KAGGLE_API_BASE_URL;
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
            FileWriter file = new FileWriter(KAGGLE_Path_DOWNLOAD + dataset_name + ".csv");
            PrintWriter write = new PrintWriter(file);
            while ((line = bufReader.readLine()) != null) {
                write.println(line);

            }
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

package com.myclinic.Downloader;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostData extends AsyncTask<String, Void, JSONObject> {
    private JSONObject postData;
    private JSONObject obj = null;

    // Change the constructor to accept a JSONObject
    public PostData(JSONObject postData) {
        this.postData = postData;
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");

            // urlConnection.setRequestProperty("Authorization", "someAuthString");

            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == 200 || statusCode == 201) {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                String response = convertInputStreamToString(inputStream);
                Log.i("RESULT", response);

                // Check if the response is a JSON array or object
                try {
                    obj = new JSONObject(response);
                } catch (Exception e) {
                    // If it's not a JSON object, try to parse it as an array
                    obj = null;
                }

            } else {
                Log.i("ERROR", "" + statusCode);
            }

        } catch (Exception e) {
            Log.d("ERROR", e.getLocalizedMessage());
            return obj;
        }

        return obj;
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

package com.myclinic.http;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetData extends AsyncTask<String, Void, JSONObject> {
    private JSONObject obj = null;

    /**
     * @param urls urls[0]: endpoint, urls[1]: token (optional)
     */
    @Override
    protected JSONObject doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            if (urls.length > 1) {
                urlConnection.setRequestProperty("Authorization", "Bearer " + urls[1]);
            }

            int statusCode = urlConnection.getResponseCode();
            Log.i("STATUS", String.valueOf(statusCode));

            InputStream inputStream;
            if (statusCode == 200 || statusCode == 201) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            InputStream responseStream = new BufferedInputStream(inputStream);

            String response = convertInputStreamToString(responseStream);
            Log.i("RESULT", response);

            // Check if the response is a JSON array or object
            try {
                obj = new JSONObject(response);
            } catch (Exception e) {
                // If it's not a JSON object, try to parse it as an array
                obj = null;
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

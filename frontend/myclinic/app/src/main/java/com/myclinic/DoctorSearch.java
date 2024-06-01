package com.myclinic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myclinic.http.Endpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DoctorSearch extends AppCompatActivity {

    private EditText editTextSearch;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_search);

        editTextSearch = findViewById(R.id.searchbox);
        progressBar = findViewById(R.id.progressBar);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void Back(View view) {
        finish();
    }

    public void searchDoctors(View view) {
        sendSearchRequest();
    }

    private void sendSearchRequest() {
        JSONObject postData = createSearchData();
        if (postData != null) {
            progressBar.setVisibility(View.VISIBLE);
            PostData task = new PostData(postData) {
                @Override
                protected void onPostExecute(JSONObject result) {
                    handleSearchResponse(result);
                }
            };
            Log.v("DOCTOR SEARCH", Endpoints.DOCTORS + "?text=" + editTextSearch.getText());
            task.execute(Endpoints.DOCTORS + "?text=" + editTextSearch.getText());
        }
    }

    private JSONObject createSearchData() {
        String query = editTextSearch.getText().toString();

        if (query.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Search query cannot be empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("query", query);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

    private void handleSearchResponse(JSONObject result) {
        progressBar.setVisibility(View.GONE);
        if (result != null && result.has("doctors")) {
            try {
                JSONArray doctors = result.getJSONArray("doctors");
                // Process the list of doctors (e.g., display them in a RecyclerView)
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No doctors found", Toast.LENGTH_SHORT).show();
        }
    }

    private abstract class PostData extends AsyncTask<String, Void, JSONObject> {
        private final JSONObject postData;

        protected PostData(JSONObject postData) {
            this.postData = postData;
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream os = connection.getOutputStream();
                os.write(postData.toString().getBytes("UTF-8"));
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return new JSONObject(sb.toString());
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

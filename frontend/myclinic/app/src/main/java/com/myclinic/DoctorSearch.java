package com.myclinic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myclinic.http.Endpoints;
import com.myclinic.http.GetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DoctorSearch extends AppCompatActivity {

    private EditText searchBox;
    private LinearLayout doctorListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_search);

        searchBox = findViewById(R.id.searchbox);
        doctorListLayout = findViewById(R.id.doctor_list);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        searchDoctors();
    }

    public void Back(View view) {
        finish();
    }

    public void searchDoctors() {
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        GetData task = new GetData() {
            @Override
            protected void onPostExecute(JSONObject result) {
                if (result != null) {
                    try {
                        JSONArray doctors = result.getJSONArray("list");
                        doctorListLayout.removeAllViews();
                        for (int i = 0; i < doctors.length(); i++) {
                            JSONObject doctor = doctors.getJSONObject(i);
                            addDoctorCard(doctor);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch doctors", Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.execute(Endpoints.DOCTORS, sharedPref.getString("token", "_"));
    }

    public void performSearch(View view) {
        String query = searchBox.getText().toString().trim();
        Log.v("QUERY", query);
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        GetData task = new GetData() {
            @Override
            protected void onPostExecute(JSONObject result) {
                if (result != null) {
                    try {
                        JSONArray doctors = result.getJSONArray("list");
                        doctorListLayout.removeAllViews();
                        for (int i = 0; i < doctors.length(); i++) {
                            JSONObject doctor = doctors.getJSONObject(i);
                            addDoctorCard(doctor);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch doctors", Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.execute(Endpoints.DOCTORS + "?text=" + query, sharedPref.getString("token", "_"));
    }

    private void addDoctorCard(JSONObject doctor) throws JSONException {
        View doctorCard = LayoutInflater.from(this).inflate(R.layout.card_doctors, doctorListLayout, false);
        TextView doctorName = doctorCard.findViewById(R.id.docname);
        TextView doctorSpecialty = doctorCard.findViewById(R.id.docspec);
        Button bookNowButton = doctorCard.findViewById(R.id.book_now_button);

        doctorName.setText(doctor.getString("name"));
        doctorSpecialty.setText(doctor.getString("specialty"));

        bookNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorSearch.this, BookVisit.class);
            try {
                intent.putExtra("doctorId", doctor.getString("id"));
                intent.putExtra("doctorName", doctor.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        });

        doctorListLayout.addView(doctorCard);
    }
}

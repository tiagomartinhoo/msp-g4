package com.myclinic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.myclinic.http.Endpoints;
import com.myclinic.http.GetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class History extends AppCompatActivity {

    private LinearLayout historyContainer;
    CheckBox appointment, exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        historyContainer = findViewById(R.id.history_container);

        appointment = findViewById(R.id.checkbox_appointment);
        appointment.setChecked(true);
        exam = findViewById(R.id.checkbox_exam);

        appointment.setOnClickListener(view -> {
            handleCheckboxClick(appointment, exam);
            fetchHistory("appointments");
        });
        exam.setOnClickListener(view -> {
            handleCheckboxClick(exam, appointment);
            fetchHistory("exams");
        });

        // Set default view to show appointments
        fetchHistory("appointments");
    }

    private void fetchHistory(String type) {
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        GetData task = new GetData() {
            @Override
            protected void onPostExecute(JSONObject result) {
                if (result != null) {
                    try {
                        JSONArray entries = result.getJSONArray("list");
                        if (entries.length() > 0) {
                            historyContainer.removeAllViews();
                            for (int i = 0; i < entries.length(); i++) {
                                JSONObject entry = entries.getJSONObject(i);
                                displayEntry(entry, type);
                            }
                        } else {
                            displayNoEntries();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    displayFetchError();
                }
            }
        };
        task.execute(type.equals("appointments") ? Endpoints.appointments(sharedPref.getString("id", "_")) : Endpoints.exams(sharedPref.getString("id", "_")),
                sharedPref.getString("token", "_"));
    }

    private void displayEntry(JSONObject entry, String type) throws JSONException {
        View entryCard = LayoutInflater.from(this).inflate(R.layout.card_pastvisit, historyContainer, false);

        TextView docNameTextView = entryCard.findViewById(R.id.docname);
        TextView docSpecTextView = entryCard.findViewById(R.id.docspec);
        TextView examTypeTextView = entryCard.findViewById(R.id.examtype);
        TextView dateTextView = entryCard.findViewById(R.id.date);
        TextView timeTextView = entryCard.findViewById(R.id.time);

        String entryTime;


        if (type.equals("appointments")) {
            docNameTextView.setText(entry.getString("doctorName"));
            docSpecTextView.setText(entry.getString("serviceName"));
            docNameTextView.setVisibility(View.VISIBLE);
            docSpecTextView.setVisibility(View.VISIBLE);
            examTypeTextView.setVisibility(View.GONE);
            entryTime = entry.getString("timeOfAppointment");
        } else {
            examTypeTextView.setText(entry.getString("examName"));
            docNameTextView.setVisibility(View.GONE);
            docSpecTextView.setVisibility(View.GONE);
            examTypeTextView.setVisibility(View.VISIBLE);
            entryTime = entry.getString("timeOfExam");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(entryTime, formatter);
        DateTimeFormatter displayFormatterDate = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter displayFormatterTime = DateTimeFormatter.ofPattern("HH:mm");

        dateTextView.setText(dateTime.format(displayFormatterDate));
        timeTextView.setText(dateTime.format(displayFormatterTime));

        historyContainer.addView(entryCard);
    }

    private void displayNoEntries() {
        historyContainer.removeAllViews();
        TextView noEntriesTextView = new TextView(this);
        noEntriesTextView.setText("No entries found");
        noEntriesTextView.setTextSize(18);
        historyContainer.addView(noEntriesTextView);
    }

    private void displayFetchError() {
        historyContainer.removeAllViews();
        TextView errorTextView = new TextView(this);
        errorTextView.setText("Failed to fetch entries");
        errorTextView.setTextSize(18);
        historyContainer.addView(errorTextView);
    }

    private void handleCheckboxClick(CheckBox selected, CheckBox other) {
        if (selected.isChecked()) {
            other.setChecked(false);
        }
    }

    public void Back(View view) {
        finish();
    }
}

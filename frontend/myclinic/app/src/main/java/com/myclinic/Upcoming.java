package com.myclinic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class Upcoming extends AppCompatActivity {

    private LinearLayout allAppointmentsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        allAppointmentsContainer = findViewById(R.id.all_appointments_container);

        fetchAllAppointments();
    }

    private void fetchAllAppointments() {
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        GetData task = new GetData() {
            @Override
            protected void onPostExecute(JSONObject result) {
                if (result != null) {
                    try {
                        JSONArray appointments = result.getJSONArray("list");
                        if (appointments.length() > 0) {
                            for (int i = 0; i < appointments.length(); i++) {
                                JSONObject appointment = appointments.getJSONObject(i);
                                displayAppointment(appointment);
                            }
                        } else {
                            displayNoAppointments();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    displayFetchError();
                }
            }
        };
        task.execute(Endpoints.appointments(sharedPref.getString("id", "_")), sharedPref.getString("token", "_"));
    }

    private void displayAppointment(JSONObject appointment) throws JSONException {
        View appointmentCard = LayoutInflater.from(this).inflate(R.layout.card_visit, allAppointmentsContainer, false);

        TextView docNameTextView = appointmentCard.findViewById(R.id.docname);
        TextView docSpecTextView = appointmentCard.findViewById(R.id.docspec);
        TextView dateTextView = appointmentCard.findViewById(R.id.date);
        TextView timeTextView = appointmentCard.findViewById(R.id.time);

        String doctorName = appointment.getString("doctorName");
        String serviceName = appointment.getString("serviceName");
        String appointmentTime = appointment.getString("timeOfAppointment");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(appointmentTime, formatter);
        DateTimeFormatter displayFormatterDate = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter displayFormatterTime = DateTimeFormatter.ofPattern("HH:mm");

        if (dateTime.isBefore(LocalDateTime.now())) return;

        docNameTextView.setText(doctorName);
        docSpecTextView.setText(serviceName);
        dateTextView.setText(dateTime.format(displayFormatterDate));
        timeTextView.setText(dateTime.format(displayFormatterTime));

        allAppointmentsContainer.addView(appointmentCard);
    }

    private void displayNoAppointments() {
        TextView noAppointmentsTextView = new TextView(this);
        noAppointmentsTextView.setText("No appointments found");
        noAppointmentsTextView.setTextSize(18);
        allAppointmentsContainer.addView(noAppointmentsTextView);
    }

    private void displayFetchError() {
        TextView errorTextView = new TextView(this);
        errorTextView.setText("Failed to fetch appointments");
        errorTextView.setTextSize(18);
        allAppointmentsContainer.addView(errorTextView);
    }

    public void Back(View view) {finish();}
}

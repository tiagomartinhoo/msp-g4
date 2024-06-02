package com.myclinic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.myclinic.http.Endpoints;
import com.myclinic.http.PostData;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Calendar;

public class BookVisit extends AppCompatActivity {

    private Button datePickerButton, timePickerButton, confirmButton, cancelButton;
    CheckBox appointment, exam;
    private Spinner serviceSpinner, doctorSpinner;
    private TextView totalPriceTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_visit);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        datePickerButton = findViewById(R.id.date_picker_button);
        timePickerButton = findViewById(R.id.time_picker_button);
        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        appointment = findViewById(R.id.checkbox_appointment);
        exam = findViewById(R.id.checkbox_exam);
        serviceSpinner = findViewById(R.id.service_spinner);
        doctorSpinner = findViewById(R.id.doctor_spinner);
        totalPriceTextView = findViewById(R.id.total_price_textview);

        datePickerButton.setOnClickListener(view -> openDatePicker());
        timePickerButton.setOnClickListener(view -> openTimePicker());
        confirmButton.setOnClickListener(view -> sendPostRequest());
        cancelButton.setOnClickListener(view -> finish());
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, day1) -> {
            String date = LocalDate.of(year1, month1 + 1, day1).toString();
            datePickerButton.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String time = String.format("%02d:%02d", hourOfDay, minute1);
            timePickerButton.setText(time + ":00");
        }, hour, minute, true);
        timePickerDialog.show();
    }


    private void sendPostRequest() {
        JSONObject postData = createPostData();
        if (postData != null) {
            PostData task = new PostData(postData) {
                @Override
                protected void onPostExecute(JSONObject result) {
                    handlePostResponse(result);
                }
            };
            SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
            task.execute(Endpoints.appointments(sharedPref.getString("id", "_")), sharedPref.getString("token", "_"));
        }
    }

    private JSONObject createPostData() {
        String date = datePickerButton.getText().toString();
        String time = timePickerButton.getText().toString();

        if (date.equals("dd-mm-yyyy") || time.equals("hh:mm")) {
            Toast.makeText(getApplicationContext(), "Please select a date and time", Toast.LENGTH_SHORT).show();
            return null;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("dID", "8928f219-28ed-40cb-8b48-276aab8d652a");
            postData.put("serviceID", "6624333d0c765121a479119c");
            postData.put("timeOfAppointment", date + "T" + time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

    private void handlePostResponse(JSONObject result) {
        if (result != null) {
            // Handle successful response
            Toast.makeText(getApplicationContext(), "Appointment Created", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Handle unsuccessful response
            Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
        }

    }

    public void Cancel(View view) {
        finish();
    }

    public void Appointment(View view) {
        if (appointment.isChecked()) {
            appointment.setChecked(true);
            exam.setChecked(false);
        } else {
            appointment.setChecked(false);
            exam.setChecked(true);
        }
    }

    public void Exam(View view) {
        if (exam.isChecked()){
            exam.setChecked(true);
            appointment.setChecked(false);
        }else{
            exam.setChecked(false);
            appointment.setChecked(true);
        }
    }
}
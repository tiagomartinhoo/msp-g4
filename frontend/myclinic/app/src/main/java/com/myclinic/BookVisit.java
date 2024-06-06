package com.myclinic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.myclinic.http.Endpoints;
import com.myclinic.http.GetData;
import com.myclinic.http.PostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class BookVisit extends AppCompatActivity {

    private Button datePickerButton, timePickerButton, confirmButton, cancelButton;
    CheckBox appointment, exam;
    private Spinner serviceSpinner, doctorSpinner;
    private TextView totalPriceTextView, textTitledoc;
    private ArrayList<Doctor> doctorList = new ArrayList<>();
    private ArrayList<Service> serviceList = new ArrayList<>();
    private String selectedDoctorId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_visit);

        // STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // NAVIGATION BAR INVISIBLE
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
        appointment.setChecked(true);
        exam = findViewById(R.id.checkbox_exam);
        serviceSpinner = findViewById(R.id.service_spinner);
        doctorSpinner = findViewById(R.id.doctor_spinner);
        textTitledoc = findViewById(R.id.textTitledoc);
        totalPriceTextView = findViewById(R.id.total_price_textview);

        datePickerButton.setOnClickListener(view -> openDatePicker());
        timePickerButton.setOnClickListener(view -> openTimePicker());
        confirmButton.setOnClickListener(view -> sendPostRequest());
        cancelButton.setOnClickListener(view -> finish());

        appointment.setOnClickListener(view -> handleCheckboxClick(appointment, exam));
        exam.setOnClickListener(view -> handleCheckboxClick(exam, appointment));

        fetchDoctors();
        fetchServices();

        // Get the doctor information from the intent
        if (getIntent() != null) {
            selectedDoctorId = getIntent().getStringExtra("doctorId");
        }
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
            String userId = sharedPref.getString("id", "_");
            String token = sharedPref.getString("token", "_");
            String endpoint;

            if (appointment.isChecked()) {
                endpoint = Endpoints.appointments(userId);
            } else if (exam.isChecked()) {
                endpoint = Endpoints.exams(userId);
            } else {
                Toast.makeText(getApplicationContext(), "Please select either Appointment or Exam", Toast.LENGTH_SHORT).show();
                return;
            }

            task.execute(endpoint, token);
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
            String dateTime = date + "T" + time;
            if (appointment.isChecked()) {
                Doctor selectedDoctor = (Doctor) doctorSpinner.getSelectedItem();
                Service selectedService = (Service) serviceSpinner.getSelectedItem();
                postData.put("dID", selectedDoctor.getId());
                postData.put("serviceID", selectedService.getId());
                postData.put("timeOfAppointment", dateTime);
            } else if (exam.isChecked()) {
                Service selectedService = (Service) serviceSpinner.getSelectedItem();
                postData.put("examID", selectedService.getId());
                postData.put("examDateTime", dateTime);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

    private void handlePostResponse(JSONObject result) {
        if (result != null) {
            Toast.makeText(getApplicationContext(), "Schedule Successful", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchDoctors() {
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        GetData task = new GetData() {
            @Override
            protected void onPostExecute(JSONObject result) {
                if (result != null) {
                    try {
                        JSONArray doctors = result.getJSONArray("list");
                        for (int i = 0; i < doctors.length(); i++) {
                            JSONObject doctor = doctors.getJSONObject(i);
                            doctorList.add(new Doctor(doctor.getString("id"), doctor.getString("name")));
                        }
                        ArrayAdapter<Doctor> adapter = new ArrayAdapter<>(BookVisit.this, android.R.layout.simple_spinner_item, doctorList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        doctorSpinner.setAdapter(adapter);

                        // Pre-select the doctor if passed via intent
                        if (selectedDoctorId != null) {
                            for (int i = 0; i < doctorList.size(); i++) {
                                if (doctorList.get(i).getId().equals(selectedDoctorId)) {
                                    doctorSpinner.setSelection(i);
                                    break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(BookVisit.this, "Failed to fetch doctors", Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.execute(Endpoints.DOCTORS, sharedPref.getString("token", "_"));
    }

    private void fetchServices() {
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        GetData task = new GetData() {
            @Override
            protected void onPostExecute(JSONObject result) {
                if (result != null) {
                    serviceList.clear();
                    try {
                        JSONArray services;
                        if (appointment.isChecked())
                            services = result.getJSONArray("services");
                        else
                            services = result.getJSONArray("exams");

                        for (int i = 0; i < services.length(); i++) {
                            JSONObject service = services.getJSONObject(i);
                            serviceList.add(new Service(service.getString("id"), service.getString("name"), service.getDouble("price")));
                        }
                        ArrayAdapter<Service> adapter = new ArrayAdapter<>(BookVisit.this, android.R.layout.simple_spinner_item, serviceList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        serviceSpinner.setAdapter(adapter);

                        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                totalPriceTextView.setText(String.format("%.2f€", serviceList.get(position).getPrice()));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                totalPriceTextView.setText("");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch services", Toast.LENGTH_SHORT).show();
                }
            }
        };
        if (appointment.isChecked())
            task.execute(Endpoints.SERVICES_AVAILABLE, sharedPref.getString("token", "_"));
        else
            task.execute(Endpoints.EXAMS_AVAILABLE, sharedPref.getString("token", "_"));
    }

    private void handleCheckboxClick(CheckBox selected, CheckBox other) {
        if (selected.isChecked()) {
            other.setChecked(false);
            if (selected == exam) {
                doctorSpinner.setVisibility(View.GONE);
                textTitledoc.setVisibility(View.GONE);
            } else {
                doctorSpinner.setVisibility(View.VISIBLE);
                textTitledoc.setVisibility(View.VISIBLE);
            }
        }
        fetchServices();
    }

    public void Cancel(View view) {
        finish();
    }
}

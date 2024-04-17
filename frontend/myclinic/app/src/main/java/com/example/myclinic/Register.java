package com.example.myclinic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.garagerent.R;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Register extends AppCompatActivity {

    private int currentStep = 0;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private ConstraintLayout cLayGeneral;
    private ConstraintLayout cLayAddressRequest;
    private ConstraintLayout cLayAddressForm;
    private ConstraintLayout cLayConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //FAZER COM QUE O TECLADO PONHA OS EDITTEXT'S PARA CIMA
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN , WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);

        //SPINNER (DROPDOWN)
        Spinner spinner = findViewById(R.id.spinnerGender);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.genders, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //DATEPICKER
        initDatePicker();
        dateButton = findViewById(R.id.datePicker);
        dateButton.setText(getTodaysDate());

        //STEPPER
        cLayGeneral = findViewById(R.id.constraintLayoutGeneral);
        cLayAddressRequest = findViewById(R.id.constraintLayoutAddressRequest);
        cLayAddressForm = findViewById(R.id.constraintLayoutAddressForm);
        cLayConfirmation = findViewById(R.id.constraintLayoutConfirmation);
        final StepView stepView = findViewById(R.id.step_view);
        findViewById(R.id.nextGen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStep++;
                cLayGeneral.setVisibility(View.INVISIBLE);
                cLayAddressRequest.setVisibility(View.VISIBLE);
                stepView.go(currentStep, true);
            }
        });
        findViewById(R.id.nextAddReq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStep++;
                cLayAddressRequest.setVisibility(View.INVISIBLE);
                cLayConfirmation.setVisibility(View.VISIBLE);
                stepView.go(currentStep, true);
            }
        });
        findViewById(R.id.nextAddForm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStep++;
                cLayAddressForm.setVisibility(View.INVISIBLE);
                cLayConfirmation.setVisibility(View.VISIBLE);
                stepView.go(currentStep, true);
            }
        });
        findViewById(R.id.nextConf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepView.done(true);
                //startActivity(new Intent(Register.this, TEMP.class));
            }
        });
        findViewById(R.id.backAddReq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep > 0) {
                    currentStep--;
                    cLayAddressRequest.setVisibility(View.INVISIBLE);
                    cLayGeneral.setVisibility(View.VISIBLE);
                }
                stepView.done(false);
                stepView.go(currentStep, true);
            }
        });
        findViewById(R.id.backConf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep > 0) {
                    currentStep--;
                    cLayConfirmation.setVisibility(View.INVISIBLE);
                    cLayAddressRequest.setVisibility(View.VISIBLE);
                }
                stepView.done(false);
                stepView.go(currentStep, true);
            }
        });
        findViewById(R.id.AddAddReq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cLayAddressRequest.setVisibility(View.INVISIBLE);
                cLayAddressForm.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.cancelAddForm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cLayAddressForm.setVisibility(View.INVISIBLE);
                cLayAddressRequest.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.cancelGen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
        List<String> steps = new ArrayList<>();
        for (int i  = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    steps.add("General");
                    break;
                case 1:
                    steps.add("Address");
                    break;
                case 2:
                    steps.add("Confirmation");
                    break;
            }
        }
        steps.set(steps.size() - 1, steps.get(steps.size() - 1));
        stepView.setSteps(steps);
    }

    //DATEPICKER
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }
    private String makeDateString(int day, int month, int year) {
        return day + "-" + month + "-" + year;
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

}
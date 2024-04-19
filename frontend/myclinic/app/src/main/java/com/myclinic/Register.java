package com.myclinic;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    private TextView textViewName, textViewPhoneNr, textViewEmail, textViewInsur, textViewNIF, textViewGender, textViewDate, textViewAdress, textViewPostalCode, textViewDoorNr;
    private EditText editTextName, editTextPhoneNr, editTextEmail, editTextInsur, editTextNIF, editTextPass, editTextAdress, editTextPostalCode, editTextDoorNr;

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

        //DECLARATIONS REGISTER
        editTextName = findViewById(R.id.editNameGen);
        editTextPhoneNr = findViewById(R.id.editPCGen);
        editTextEmail = findViewById(R.id.editEmailGen);
        editTextInsur = findViewById(R.id.editinsurance);
        editTextNIF = findViewById(R.id.editNIFGen);
        editTextPass = findViewById(R.id.editPasswordGen);
        editTextAdress = findViewById(R.id.editAddressAddForm);
        editTextPostalCode = findViewById(R.id.editPCAddForm);
        editTextDoorNr = findViewById(R.id.editDNAddForm);

        textViewName = findViewById(R.id.textNameConf);
        textViewPhoneNr = findViewById(R.id.textPhoneConf);
        textViewEmail = findViewById(R.id.textEmailConf);
        textViewInsur = findViewById(R.id.textInsuranceConf);
        textViewNIF = findViewById(R.id.textNIFConf);
        textViewGender = findViewById(R.id.textGenderConf);
        textViewDate = findViewById(R.id.textBDConf);
        textViewAdress = findViewById(R.id.textAddressConf);
        textViewPostalCode = findViewById(R.id.textPCConf);
        textViewDoorNr = findViewById(R.id.textDNConf);

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
                if(!editTextName.getText().toString().equals("") &&
                        !editTextPhoneNr.getText().toString().equals("") &&
                        !editTextEmail.getText().toString().equals("") &&
                        !editTextInsur.getText().toString().equals("") &&
                        !editTextNIF.getText().toString().equals("") &&
                        !editTextPass.getText().toString().equals("")){
                    currentStep++;
                    cLayGeneral.setVisibility(View.GONE);
                    cLayAddressRequest.setVisibility(View.VISIBLE);
                    stepView.go(currentStep, true);
                }else{
                    Toast.makeText(Register.this, "Empty Inputs" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.nextAddReq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewName.setText(editTextName.getText().toString());
                textViewEmail.setText(editTextEmail.getText().toString());
                textViewInsur.setText(editTextInsur.getText().toString());
                textViewNIF.setText(editTextNIF.getText().toString());
                textViewPhoneNr.setText(editTextPhoneNr.getText().toString());
                textViewGender.setText(spinner.getSelectedItem().toString());
                textViewDate.setText(dateButton.getText().toString());
                if(editTextAdress.getText().toString().equals("")){
                    textViewAdress.setText("Empty");
                }else{textViewAdress.setText(editTextAdress.getText().toString());}
                if(editTextPostalCode.getText().toString().equals("")){
                    textViewPostalCode.setText("Empty");
                }else{textViewPostalCode.setText(editTextPostalCode.getText().toString());}
                if(editTextDoorNr.getText().toString().equals("")){
                    textViewDoorNr.setText("Empty");
                }else{textViewDoorNr.setText(editTextDoorNr.getText().toString());}

                currentStep++;
                cLayAddressRequest.setVisibility(View.GONE);
                cLayConfirmation.setVisibility(View.VISIBLE);
                stepView.go(currentStep, true);
            }
        });
        findViewById(R.id.nextAddForm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewName.setText(editTextName.getText().toString());
                textViewEmail.setText(editTextEmail.getText().toString());
                textViewInsur.setText(editTextInsur.getText().toString());
                textViewNIF.setText(editTextNIF.getText().toString());
                textViewPhoneNr.setText(editTextPhoneNr.getText().toString());
                textViewGender.setText(spinner.getSelectedItem().toString());
                textViewDate.setText(dateButton.getText().toString());
                if(editTextAdress.getText().toString().equals("")){
                    textViewAdress.setText("Empty");
                }else{textViewAdress.setText(editTextAdress.getText().toString());}
                if(editTextPostalCode.getText().toString().equals("")){
                    textViewPostalCode.setText("Empty");
                }else{textViewPostalCode.setText(editTextPostalCode.getText().toString());}
                if(editTextDoorNr.getText().toString().equals("")){
                    textViewDoorNr.setText("Empty");
                }else{textViewDoorNr.setText(editTextDoorNr.getText().toString());}

                currentStep++;
                cLayAddressForm.setVisibility(View.GONE);
                cLayConfirmation.setVisibility(View.VISIBLE);
                stepView.go(currentStep, true);
            }
        });
        findViewById(R.id.nextConf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BOTÃO FINAL DE REGISTER
                stepView.done(true);
                startActivity(new Intent(Register.this, MainActivity.class));
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
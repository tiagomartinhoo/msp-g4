package com.example.myclinic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.Calendar;

public class CreateRent extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private NumberPicker weekdayPicker, monthdayPicker;
    String[] weekdays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private ConstraintLayout DateSingle, DateRange, WeekPicker, MonthPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rent);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //LAYOUTS FOR SPINNER SCHEDULE
        DateSingle = findViewById(R.id.constraintLayout18);
        DateRange = findViewById(R.id.constraintLayout19);
        WeekPicker = findViewById(R.id.constraintLayout23);
        MonthPicker = findViewById(R.id.constraintLayout24);

        //SPINNER RENTAL TYPE (DROPDOWN)
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.rental, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        DateRange.setVisibility(View.GONE);
                        WeekPicker.setVisibility(View.GONE);
                        MonthPicker.setVisibility(View.GONE);
                        DateSingle.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        WeekPicker.setVisibility(View.GONE);
                        MonthPicker.setVisibility(View.GONE);
                        DateSingle.setVisibility(View.GONE);
                        DateRange.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        DateSingle.setVisibility(View.GONE);
                        MonthPicker.setVisibility(View.GONE);
                        DateRange.setVisibility(View.VISIBLE);
                        WeekPicker.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        DateSingle.setVisibility(View.GONE);
                        WeekPicker.setVisibility(View.GONE);
                        DateRange.setVisibility(View.VISIBLE);
                        MonthPicker.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                DateRange.setVisibility(View.GONE);
                WeekPicker.setVisibility(View.GONE);
                MonthPicker.setVisibility(View.GONE);
                DateSingle.setVisibility(View.VISIBLE);
            }

        });

        //DATEPICKER
        initDatePicker();
        dateButton = findViewById(R.id.datePicker);
        dateButton.setText(getTodaysDate());

        //NUMBER PICKER
        weekdayPicker = findViewById(R.id.numberPicker);
        weekdayPicker.setMinValue(0);
        weekdayPicker.setMaxValue(6);
        weekdayPicker.setDisplayedValues(weekdays);
        monthdayPicker = findViewById(R.id.numberPicker2);
        monthdayPicker.setMinValue(1);
        monthdayPicker.setMaxValue(31);
    }

    //BTN GO BACK
    public void Back(View view) {finish();}

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
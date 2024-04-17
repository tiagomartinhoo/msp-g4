package com.example.myclinic;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class EditSpot extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_spot);

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

        //SPINNER HEIGHT (DROPDOWN)
        Spinner spinnerH = findViewById(R.id.spinner1);
        ArrayAdapter adapterH = ArrayAdapter.createFromResource(this, R.array.entrance, R.layout.spinner_item);
        adapterH.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerH.setAdapter(adapterH);

        //SPINNER WIDTH (DROPDOWN)
        Spinner spinnerW = findViewById(R.id.spinner2);
        ArrayAdapter adapterW = ArrayAdapter.createFromResource(this, R.array.entrance, R.layout.spinner_item);
        adapterW.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerW.setAdapter(adapterW);

        //SPINNER TYPE PRIVACY (DROPDOWN)
        Spinner spinnerP = findViewById(R.id.spinner3);
        ArrayAdapter adapterP = ArrayAdapter.createFromResource(this, R.array.type1, R.layout.spinner_item);
        adapterP.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerP.setAdapter(adapterP);

        //SPINNER TYPE LOCATION (DROPDOWN)
        Spinner spinnerL = findViewById(R.id.spinner4);
        ArrayAdapter adapterL = ArrayAdapter.createFromResource(this, R.array.type2, R.layout.spinner_item);
        adapterL.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerL.setAdapter(adapterL);
    }

    //BTN GO BACK
    public void Back(View view) {finish();}
}
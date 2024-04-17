package com.example.myclinic;


import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myclinic.Downloader.PostData2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateSpot extends AppCompatActivity {

    private EditText editTextName, editTextAddress, editTextPrice, editTextNr;
    private Button button;
    private String URL = "https://garage-rent-api.azurewebsites.net/api/Garage/CreateGarage";
    static final String email_user = "email";
    static final String pass_user = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);

        SharedPreferences sharedPref = this.getSharedPreferences("login", MODE_PRIVATE);

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

        //DECLARATIONS
        editTextName = findViewById(R.id.editEmailGen);
        editTextAddress = findViewById(R.id.editAddressAddForm);
        editTextPrice = findViewById(R.id.editPC);
        editTextNr = findViewById(R.id.edit2);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!editTextName.getText().toString().equals("") &&
                        !editTextAddress.getText().toString().equals("") &&
                        !editTextPrice.getText().toString().equals("") &&
                        !editTextNr.getText().toString().equals("")){

                    Map<String, String> postData = new HashMap<>();
                    postData.put("email", sharedPref.getString("email_user", email_user));
                    postData.put("pass", sharedPref.getString("pass_user", pass_user));
                    postData.put("name", editTextName.getText().toString());

                    postData.put("address", editTextAddress.getText().toString());
                    postData.put("price", editTextPrice.getText().toString());
                    postData.put("spaces", editTextNr.getText().toString());
                    postData.put("latitude", "38.63");
                    postData.put("longitude", "-9.095");
                    postData.put("ownerId", "12");


                    PostData2 task = new PostData2(postData);


                    try {
                        task.execute(URL).get();
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    Toast.makeText(CreateSpot.this, "Garage Added", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(CreateSpot.this, "Empty Inputs", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    //BTN GO BACK
    public void Back(View view) {finish();}
}
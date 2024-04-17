package com.example.myclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myclinic.Downloader.PostData;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private EditText editTextEmail, editTextPw;
    private Button button;
    private String URL = "https://garage-rent-api.azurewebsites.net/api/Account/Login";



    static final String id = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // SHARED PREFERENCES
        SharedPreferences sharedPref = this.getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // FAZER COM QUE O TECLADO PONHA OS EDITTEXT'S PARA CIMA
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);

        // DECLARATIONS LOGIN
        editTextEmail = findViewById(R.id.email);
        editTextPw = findViewById(R.id.password);
        button = findViewById(R.id.buttonLogin);

        // LOGIN
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject postData = new JSONObject();
                try {
                    postData.put("email", editTextEmail.getText().toString());
                    postData.put("password", editTextPw.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                PostData task = new PostData(postData) {
                    @Override
                    protected void onPostExecute(JSONObject result) {
                        super.onPostExecute(result);

                        // Log the response for debugging
                        Log.d("LoginResponse", result != null ? result.toString() : "null");

                        // Handle the result here
                        if (result != null && result.has("id")) {
                            // Successfully logged in
                            // Save user credentials to SharedPreferences
                            try {
                                editor.putString("id", result.getString("id")).apply();
                                Log.v("Login", sharedPref.getString("id", id));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            //editor.putString("pass_user", editTextPw.getText().toString()).apply();
                            startActivity(new Intent(Login.this, UserTypePick.class));
                        } else {
                            // Login unsuccessful
                            Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                task.execute(URL);
            }
        });
    }

    // REGISTER INTENT
    public void Register(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }




}

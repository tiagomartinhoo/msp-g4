package com.myclinic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myclinic.http.Endpoints;
import com.myclinic.http.PostData;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterDoctor extends AppCompatActivity {
    private EditText editTextName, editTextPhoneNr, editTextEmail, editTextNIF, editTextPass, editTextSpecialty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        // SHARED PREFERENCES
        SharedPreferences sharedPref = this.getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN , WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);

        //DECLARATIONS REGISTER
        editTextName = findViewById(R.id.editNameGen);
        editTextPhoneNr = findViewById(R.id.editPCGen);
        editTextEmail = findViewById(R.id.editEmailGen);
        editTextNIF = findViewById(R.id.editNIFGen);
        editTextPass = findViewById(R.id.editPassGen);
        editTextSpecialty = findViewById(R.id.editSpecialtyGen);

        findViewById(R.id.nextGen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                sendRegisterRequest();
            }


            private void startMainActivity () {
                Intent intent = new Intent(RegisterDoctor.this, Admin.class);
                startActivity(intent);
            }

            private void sendRegisterRequest () {
                JSONObject postData = createRegisterData();
                if (postData != null) {
                    PostData task = new PostData(postData) {
                        @Override
                        protected void onPostExecute(JSONObject result) {
                            handleLoginResponse(result);
                        }
                    };
                    task.execute(Endpoints.DOCTORS);
                }
            }

            private JSONObject createRegisterData () {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String phoneNumber = editTextPhoneNr.getText().toString();
                String password = editTextPass.getText().toString();
                String nif = editTextNIF.getText().toString();
                String specialty = editTextSpecialty.getText().toString();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("name", name);
                    postData.put("email", email);
                    postData.put("phoneNumber", phoneNumber);
                    postData.put("password", password);
                    postData.put("nif", nif);
                    postData.put("specialty", specialty);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return postData;
            }

            private void handleLoginResponse (JSONObject result){
                //progressBar.setVisibility(View.GONE);
                if (result != null && result.has("token")) {
                    // Successfully register
                    saveUserCredentials(result);
                    startMainActivity();
                } else {
                    // Register unsuccessful
                    try {
                        if (result != null && result.has("title") && result.has("status")) {
                            Toast.makeText(getApplicationContext(), result.getString("title"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Register", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            private void saveUserCredentials (JSONObject result){
                String token;
                try {
                    token = result.getString("token");
                    editor.putString("token", token).apply();
                    Log.v("Register", sharedPref.getString("token", token));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void Cancel(View view) {
        finish();
    }

}
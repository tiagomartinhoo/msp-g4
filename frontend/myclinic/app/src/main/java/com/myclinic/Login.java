package com.myclinic;

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
import android.widget.ProgressBar;
import android.widget.Toast;
import com.myclinic.Downloader.Endpoints;
import com.myclinic.Downloader.PostData;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private EditText editTextEmail, editTextPw;
    private Button button;
    private ProgressBar progressBar;
    private final String URL = Endpoints.LOGIN_ENDPOINT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.progressBar);

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
                progressBar.setVisibility(View.VISIBLE);
                // Send login request to the backend
                sendLoginRequest();
            }

            private void startMainActivity() {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }

            private void sendLoginRequest() {
                JSONObject postData = createLoginData();
                if (postData != null) {
                    PostData task = new PostData(postData) {
                        @Override
                        protected void onPostExecute(JSONObject result) {
                            handleLoginResponse(result);
                        }
                    };
                    task.execute(URL);
                }
            }

            private JSONObject createLoginData() {
                String email = editTextEmail.getText().toString();
                String password = editTextPw.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                    return null;
                }

                JSONObject postData = new JSONObject();
                try {
                    postData.put("email", email);
                    postData.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return postData;
            }

            private void handleLoginResponse(JSONObject result) {
                progressBar.setVisibility(View.GONE);
                if (result != null && result.has("token")) {
                    // Successfully logged in
                    saveUserCredentials(result);
                    startMainActivity();
                } else {
                    // Login unsuccessful
                    Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_SHORT).show();
                }
            }

            private void saveUserCredentials(JSONObject result) {
                String token;
                try {
                    token = result.getString("token");
                    editor.putString("token", token).apply();
                    Log.v("Login", sharedPref.getString("token", token));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    // REGISTER INTENT
    public void Register(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }

}

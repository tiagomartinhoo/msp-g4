package com.myclinic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the user is logged in
                boolean isLoggedIn = checkUserLoggedIn();

                // Navigate to the appropriate screen
                if (isLoggedIn) {
                    navigateToMainActivity();
                } else {
                    navigateToLoginActivity();
                }
                finish();
            }
        }, SPLASH_DELAY);
    }

    private boolean checkUserLoggedIn() {
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        // sharedPref.edit().clear().apply();
        return sharedPref.contains("token") && sharedPref.contains("role");
    }

    private void navigateToMainActivity() {
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        Intent intent;
        String role = sharedPref.getString("role", "_");
        if (role.equals("PATIENT")) {
            intent = new Intent(this, MainActivity.class);
        } else if (role.equals("ADMIN")) {
            intent = new Intent(this, Admin.class);
        } else {
            intent = new Intent(this, Upcoming.class);
        }
        startActivity(intent);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}


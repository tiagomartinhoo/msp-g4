package com.example.myclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.myclinic.Maps.GPSUtils;

public class UserTypePick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_pick);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        GPSUtils.getInstance().findDeviceLocation(this);
    }

    public void LoginClient(View view) {
        startActivity(new Intent(UserTypePick.this, MainActivity.class));
    }

    public void LoginOwner(View view) {
        startActivity(new Intent(UserTypePick.this, MainActivity2.class));
    }
}
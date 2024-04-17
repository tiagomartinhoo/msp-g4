package com.example.myclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SettingsClient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_client);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    //BTN GO BACK
    public void Back(View view) {finish();}

    //BOTAO PARA PÁGINA DE USER TYPE
    public void ChangeType(View view) {startActivity(new Intent(SettingsClient.this, UserTypePick.class));}

    //BOTAO PARA PÁGINA DE LOGIN
    public void LogOut(View view) {startActivity(new Intent(SettingsClient.this, Login.class));}

    //BOTAO PARA PÁGINA DE EDIT USER
    public void EditProfile(View view) {startActivity(new Intent(SettingsClient.this, EditProfile.class));}
}
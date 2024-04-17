package com.example.myclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MyParkingSpots extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parking_spots);

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

    //DELETE PARKING SPOT
    public void DeleteSpot(View view) {showDialogDeleteSpot();}
    private void showDialogDeleteSpot(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_deleteres);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        View btnNo = dialog.findViewById(R.id.btnNo);
        View btnYes = dialog.findViewById(R.id.btnYes);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyParkingSpots.this,"No was clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyParkingSpots.this,"Yes was clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    //BOTAO PARA PÁGINA DE CRIAR SPOT
    public void CreatePS(View view) {startActivity(new Intent(MyParkingSpots.this, CreateSpot.class));}

    public void EditPS(View view) {startActivity(new Intent(MyParkingSpots.this, EditSpot.class));}

    public void Details(View view) {startActivity(new Intent(MyParkingSpots.this, Details.class));}
}
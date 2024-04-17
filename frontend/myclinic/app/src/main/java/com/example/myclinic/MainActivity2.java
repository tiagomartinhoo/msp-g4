package com.example.myclinic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.myclinic.Fragment.FragmentMap;
import com.example.myclinic.Fragment.FragmentParkingSpots;

public class MainActivity2 extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //BOTTOM NAV
        bottomNavigation = findViewById(R.id.navigation);
        bottomNavigation.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_map));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_garage));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener(){
            @Override
            public void onShowItem(MeowBottomNavigation.Model item){
                Fragment fragment = null;

                switch (item.getId()){
                    case 1:
                        fragment = new FragmentMap();
                        break;
                    case 2:
                        fragment = new FragmentParkingSpots();
                        break;
                }

                fragmentLoad(fragment);
            }
        });
        bottomNavigation.show(2,true);
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {}
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {}
        });
    }

    //BOTTOM NAV
    private void fragmentLoad(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

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
                Toast.makeText(MainActivity2.this,"No was clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2.this,"Yes was clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    //BOTAO PARA PÁGINA DE CRIAR SPOT
    public void CreatePS(View view) {startActivity(new Intent(MainActivity2.this, CreateSpot.class));}

    //BOTAO PARA PÁGINA DE EDITAR SPOT
    public void EditPS(View view) {startActivity(new Intent(MainActivity2.this, EditSpot.class));}

    //BOTAO PARA PÁGINA DE DETAILS
    public void Details(View view) {startActivity(new Intent(MainActivity2.this, Details.class));}

    //BOTAO PARA PÁGINA DE SETTINGS
    public void Settings(View view) {startActivity(new Intent(MainActivity2.this, SettingsOwner.class));}

    //PAGINA CHATS
    public void OpenChats(View view) {startActivity(new Intent(MainActivity2.this, ChatList.class));}
}
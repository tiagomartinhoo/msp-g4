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
import com.example.myclinic.Fragment.FragmentActive;
import com.example.myclinic.Fragment.FragmentExplore;
import com.example.myclinic.Fragment.FragmentPast;
import com.google.android.material.slider.RangeSlider;

public class MainActivity extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;
    RangeSlider rangeSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_history));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_discovery));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_active));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener(){
            @Override
            public void onShowItem(MeowBottomNavigation.Model item){
                Fragment fragment = null;

                switch (item.getId()){
                    case 1:
                        fragment = new FragmentPast();
                        break;
                    case 2:
                        fragment = new FragmentExplore();
                        break;
                    case 3:
                        fragment = new FragmentActive();
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

        /*RANGESLIDER
        rangeSlider = findViewById(R.id.sliderRange);
        rangeSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                currencyFormat.setCurrency(Currency.getInstance("€"));
                return currencyFormat.format(value);
            }
        });*/

    }

    //BOTTOM NAV
    private void fragmentLoad(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    //CANCELAR RESERVA
    public void CancelReservation(View view) {showDialogCancelResFirst();}
    private void showDialogCancelResFirst(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cancelres_first);
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
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showDialogCancelResSecond();
            }
        });
    }
    private void showDialogCancelResSecond(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cancelres_second);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        View btnBack = dialog.findViewById(R.id.btnBack);
        View btnCancel = dialog.findViewById(R.id.btnCancel);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Back was clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Res Canceled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    //SHOW FILTERS
    public void ShowFilters(View view) {showDialogShowFilters();}
    private void showDialogShowFilters(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_showfilters);
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
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //GIVE RATING
    public void GiveRating(View view) {showDialogGiveRating();}
    private void showDialogGiveRating(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_giverating);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        View btnCancel = dialog.findViewById(R.id.btnCancel);
        View btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //PAGINA EDIT PROFILE
    public void EditProfile(View view) {startActivity(new Intent(MainActivity.this, EditProfile.class));}

    //PAGINA EDIT PROFILE
    public void Details(View view) {startActivity(new Intent(MainActivity.this, Details.class));}

    //BOTAO PARA PÁGINA DE SETTINGS
    public void Settings(View view) {startActivity(new Intent(MainActivity.this, SettingsClient.class));}

    //PAGINA CHATS
    public void OpenChats(View view) {startActivity(new Intent(MainActivity.this, ChatList.class));}
}
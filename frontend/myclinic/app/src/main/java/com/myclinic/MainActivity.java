package com.myclinic;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.myclinic.http.Endpoints;
import com.myclinic.http.PostData;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout cLayCheckinCard, cLayCheckinNum;
    private TextView ticketTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        cLayCheckinCard = findViewById(R.id.checkincard);
        cLayCheckinNum = findViewById(R.id.checkincard2);
        ticketTextView = findViewById(R.id.ticket);
    }

    //CHECKINDIALOG
    public void Checkin(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_checkin);
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
                sendCheckinRequest();
                dialog.dismiss();
            }
        });
    }

    //SEND CHECKIN REQUEST
    private void sendCheckinRequest() {

        PostData task = new PostData(new JSONObject()) {
            @Override
            protected void onPostExecute(JSONObject result) {
                handleCheckinResponse(result);
            }
        };
        task.execute(Endpoints.checkIn("2bffadd5-8ff3-4c03-85bf-f41f8f701f46"), getSharedPreferences("login", MODE_PRIVATE).getString("token", "_"));

    }

    private void handleCheckinResponse(JSONObject result) {
        if (result != null) {
            try {
                if (result.has("number")) {
                    cLayCheckinCard.setVisibility(View.GONE);
                    cLayCheckinNum.setVisibility(View.VISIBLE);
                    String number = result.getString("number");
                    String formattedTicketNumber = String.format("%03d", Integer.parseInt(number));
                    ticketTextView.setText(String.format("G - %s", formattedTicketNumber));
                    Toast.makeText(getApplicationContext(), "Check-in successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), result.getString("title"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No response from server", Toast.LENGTH_SHORT).show();
        }
    }

    //MARCAR VISITA
    public void Schedule(View view) {
        startActivity(new Intent(MainActivity.this,BookVisit.class));
    }

    //LOG OUT
    public void Logout(View view) {
        getSharedPreferences("login", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
        startActivity(intent);
        finish();
    }


    //CANCELAR VISITA
    public void CancelVisit(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cancelvisit);
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
                Toast.makeText(MainActivity.this,"No was clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Yes was clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    //UPCOMING VISIT LIST INTENT
    public void Upcoming(View view) {
        startActivity(new Intent(MainActivity.this,Upcoming.class));
    }

    public void DoctorSearch(View view) {startActivity(new Intent(MainActivity.this,DoctorSearch.class));
    }

    public void MedReminder(View view) {startActivity(new Intent(MainActivity.this,MedReminder.class));
    }

    public void History(View view) {startActivity(new Intent(MainActivity.this,History.class));
    }

    public void AvailableServices(View view) {startActivity(new Intent(MainActivity.this,AvailableServices.class));
    }
}
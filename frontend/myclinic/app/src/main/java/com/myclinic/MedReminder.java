package com.myclinic;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Map;

public class MedReminder extends AppCompatActivity {

    private static final String PREFS_NAME = "MedReminderPrefs";
    private static final String REMINDER_KEY = "reminder_";
    private static final String REMINDER_ID_KEY = "reminder_id";

    private static final String CHANNEL_ID = "reminder_channel";

    private LinearLayout reminderContainer;
    private EditText medicationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_reminder);

        // STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        reminderContainer = findViewById(R.id.reminder_container);

        loadReminders();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder Channel";
            String description = "Channel for Medication Reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    // Load reminders from SharedPreferences
    private void loadReminders() {

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allReminders = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allReminders.entrySet()) {
            if (entry.getKey().startsWith(REMINDER_KEY) && entry.getValue() instanceof String) {
                String[] reminderDetails = ((String) entry.getValue()).split(",");
                String name = reminderDetails[0];
                String time = reminderDetails[1];
                String days = reminderDetails[2];
                addReminderCard(entry.getKey(), name, time, days);
            }
        }
    }

    // Save reminder to SharedPreferences
    private void saveReminder(int id, String name, String time, String days) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REMINDER_KEY + id, name + "," + time + "," + days);
        editor.apply();
    }

    // Get reminder from SharedPreferences
    private String getReminder(int id) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(REMINDER_KEY + id, null);
    }

    // Schedule reminder notification
    private void scheduleNotification(int id, int hour, int minute, String days, String name) {
        Log.d("MedReminder", "Scheduling notification: id=" + id + ", hour=" + hour + ", minute=" + minute + ", days=" + days);
        String[] dayArray = days.split(",");
        for (String day : dayArray) {
            int dayOfWeek = Integer.parseInt(day.trim());

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }

            Intent intent = new Intent(this, NotificationReceiver.class);
            intent.putExtra("reminder_id", id);
            intent.putExtra("medication_name", name);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id * 10 + dayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                Log.d("MedReminder", "Alarm set for: " + calendar.getTime().toString());
            }
        }
    }

    // Delete reminder notification
    private void deleteNotification(int id) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    // Generate unique ID for each reminder
    private int generateUniqueReminderId() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int id = sharedPreferences.getInt(REMINDER_ID_KEY, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(REMINDER_ID_KEY, id + 1);
        editor.apply();
        return id;
    }

    // Add reminder card to the container
    private void addReminderCard(String reminderId, String name, String time, String days) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View card = inflater.inflate(R.layout.card_reminder, reminderContainer, false);

        TextView reminderName = card.findViewById(R.id.reminder_name);
        TextView reminderTime = card.findViewById(R.id.reminder_time);
        TextView reminderDays = card.findViewById(R.id.reminder_days);

        reminderName.setText(name);
        reminderTime.setText("Time: " + time);
        reminderDays.setText("Days: " + days);

        reminderContainer.addView(card);
    }

    // DELETE REMINDER
    public void DeleteReminder(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delreminder);
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
                Toast.makeText(MedReminder.this, "No was clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MedReminder.this, "Yes was clicked", Toast.LENGTH_SHORT).show();
                // Delete the reminder
                deleteNotification(1); // Use appropriate ID
                dialog.dismiss();
            }
        });
    }

    // CREATE REMINDER
    public void CreateReminder(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_newreminder);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        View btnNo = dialog.findViewById(R.id.btnCancel);
        View btnYes = dialog.findViewById(R.id.btnCreate);

        medicationName = dialog.findViewById(R.id.editEmailGen);

        TimePicker timePicker = dialog.findViewById(R.id.timePicker);

        CheckBox checkSunday = dialog.findViewById(R.id.checkSunday);
        CheckBox checkMonday = dialog.findViewById(R.id.checkMonday);
        CheckBox checkTuesday = dialog.findViewById(R.id.checkTuesday);
        CheckBox checkWednesday = dialog.findViewById(R.id.checkWednesday);
        CheckBox checkThursday = dialog.findViewById(R.id.checkThursday);
        CheckBox checkFriday = dialog.findViewById(R.id.checkFriday);
        CheckBox checkSaturday = dialog.findViewById(R.id.checkSaturday);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MedReminder.this, "Cancel was clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                StringBuilder days = new StringBuilder();

                if (checkSunday.isChecked()) days.append("1,");
                if (checkMonday.isChecked()) days.append("2,");
                if (checkTuesday.isChecked()) days.append("3,");
                if (checkWednesday.isChecked()) days.append("4,");
                if (checkThursday.isChecked()) days.append("5,");
                if (checkFriday.isChecked()) days.append("6,");
                if (checkSaturday.isChecked()) days.append("7,");

                if (days.length() > 0) {
                    days.deleteCharAt(days.length() - 1); // Remove last comma
                }

                if (days.length() == 0) {
                    Toast.makeText(MedReminder.this, "Please select at least one day", Toast.LENGTH_SHORT).show();
                } else {
                    String daysString = days.toString();
                    Toast.makeText(MedReminder.this, "Reminder created", Toast.LENGTH_SHORT).show();
                    // Save the reminder and schedule notification
                    int reminderId = generateUniqueReminderId();
                    saveReminder(reminderId, medicationName.getText().toString(), hour + ":" + minute, daysString);
                    scheduleNotification(reminderId, hour, minute, daysString, medicationName.getText().toString());
                    addReminderCard(String.valueOf(reminderId), medicationName.getText().toString(), hour + ":" + minute, daysString);
                    dialog.dismiss();
                }
            }
        });
    }

    public void Back(View view) {
        finish();
    }
}
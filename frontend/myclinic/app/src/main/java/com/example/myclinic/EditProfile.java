package com.example.myclinic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myclinic.Models.ModelProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;

public class EditProfile extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    //FIREBASE IMAGE UPLOAD
    private ImageButton choose;
    private Button upload;
    private EditText text;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private int request_Code = 10;
    private Uri uri;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //STATUS BAR INVISIBLE
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //NAVIGATION BAR INVISIBLE
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //FAZER COM QUE O TECLADO PONHA OS EDITTEXT'S PARA CIMA
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN , WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);

        //SPINNER (DROPDOWN)
        Spinner spinner = findViewById(R.id.spinnerGender);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.genders, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //DATEPICKER
        initDatePicker();
        dateButton = findViewById(R.id.datePicker);
        dateButton.setText(getTodaysDate());

        //FIREBASE IMAGE UPLOAD
        choose = findViewById(R.id.choose);
        text = findViewById(R.id.editNameGen);
        upload = findViewById(R.id.confirmEdit);
        storageReference = FirebaseStorage.getInstance().getReference("Profile");
        databaseReference = FirebaseDatabase.getInstance().getReference("Profile Image");

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),request_Code);
            }
        });
        
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_Code && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                choose.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getFileExtention (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void Upload() {
        if (uri != null){
            StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + "." + getFileExtention(uri));
            storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String ImageName = text.getText().toString().trim();
                    text.getText().clear();
                    Toast.makeText(EditProfile.this, "upload success", Toast.LENGTH_SHORT).show();
                    ModelProfile model = new ModelProfile(ImageName, taskSnapshot.getUploadSessionUri().toString());
                    String ImageUploadId= databaseReference.push().getKey();
                    databaseReference.child(ImageUploadId).setValue(model);
                }
            });
        }
    }


    //DATEPICKER
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }
    private String makeDateString(int day, int month, int year) {
        return day + "-" + month + "-" + year;
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    //BTN GO BACK
    public void Back(View view) {finish();}


}
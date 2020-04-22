package com.example.homeworktwo.View;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.homeworktwo.Model.ImageUtil;
import com.example.homeworktwo.Model.LoginData;
import com.example.homeworktwo.Model.User;
import com.example.homeworktwo.R;


public class ViewProfile extends AppCompatActivity {

    private LoginData user;
    private Toolbar toolbar;
    private TextView textViewName,textViewEmail,textViewPhone;
    private ImageView imageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        textViewName = findViewById(R.id.tvProfileName);
        textViewEmail = findViewById(R.id.tvProfileEmail);
        textViewPhone = findViewById(R.id.tvProfilePhone);
        imageViewProfile = findViewById(R.id.proImage);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        user = (LoginData) getIntent().getSerializableExtra("User");

        if (user!=null){
            textViewName.setText(user.getName());
            textViewPhone.setText(user.getPhone());
            textViewEmail.setText(user.getEmail());
            if (user.getImage()!=null){
                Bitmap image = ImageUtil.convert(user.getImage());
                imageViewProfile.setImageBitmap(image);
            }else {
                imageViewProfile.setBackgroundResource(R.drawable.user_bac);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}

package com.example.homeworktwo.View;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.homeworktwo.Model.ImageUtil;
import com.example.homeworktwo.Model.LoginData;
import com.example.homeworktwo.Model.RegStatus;
import com.example.homeworktwo.Model.User;
import com.example.homeworktwo.R;
import com.example.homeworktwo.UserDataBase;
import com.example.homeworktwo.ViewModel.ApiClint;
import com.example.homeworktwo.ViewModel.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDashboard extends AppCompatActivity {

    private User user;

    private LoginData loginData;

    private Dialog dialog;

    private TextView textViewName,textViewEmail,textViewPhone;
    private FloatingActionButton fabPassword,fabName,fabEmail,fabPhone,fabProImage;
    private ImageView imageViewProfile;
    private Button buttonProfile;
    private Toolbar toolbar;
    Bitmap bitmap;
    private int m = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_dashboard);

        textViewName = findViewById(R.id.tvProfileName);
        textViewEmail = findViewById(R.id.tvProfileEmail);
        textViewPhone = findViewById(R.id.tvProfilePhone);
        fabPassword = findViewById(R.id.fabChangePassword);
        fabName = findViewById(R.id.fabChangeName);
        fabEmail = findViewById(R.id.fabChangeEmail);
        fabPhone = findViewById(R.id.fabChangePhone);
        fabProImage = findViewById(R.id.update_profile_image_btn);
        imageViewProfile = findViewById(R.id.proImage);
        buttonProfile = findViewById(R.id.btnViewProfile);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("DashBoard");
        setSupportActionBar(toolbar);




       // user = (User) getIntent().getSerializableExtra("User");
        loginData = (LoginData)getIntent().getSerializableExtra("User");
        Toast.makeText(this, String.valueOf(loginData.getChecekedValue()), Toast.LENGTH_SHORT).show();
        String uuid = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
//        m = user.getCheckValue();
//
//
//        user.setUuid(uuid);
//        user.setCheckValue(m);
//        UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);


        if (loginData!= null){
            textViewName.setText(loginData.getName());
            textViewEmail.setText(loginData.getEmail());
            textViewPhone.setText(loginData.getPhone());
           //Toast.makeText(this, user.getUuid(), Toast.LENGTH_SHORT).show();
            if (loginData.getImage().equals("image")){
                imageViewProfile.setBackgroundResource(R.drawable.user_bac);
            }else{

                Bitmap image = ImageUtil.convert(loginData.getImage());
                imageViewProfile.setImageBitmap(image);
            }
        }

        fabPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPasswordDIalog();
            }
        });

        fabName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNameDialog();
            }
        });

        fabEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEmailDialog();
            }
        });

        fabPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPhoneDialog();
            }
        });

        fabProImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage(ProfileDashboard.this);
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileDashboard.this,ViewProfile.class);
                intent.putExtra("User", (Serializable) loginData);
                startActivity(intent);
            }
        });
    }

    private void SelectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        bitmap= (Bitmap) data.getExtras().get("data");
                        imageViewProfile.setImageBitmap(bitmap);
                        Upload();
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                            imageViewProfile.setImageBitmap(bitmap);
                            if (bitmap==null){

                            }else {
                                Upload();
                            }
                            // img.setVisibility(View.VISIBLE);
                            //phone.setVisibility(View.VISIBLE);
                            //uploadButton.setEnabled(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                imageViewProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
//                        }

                    }
                    break;
            }
        }
    }

    private void Upload() {
//        Bitmap bitmapImage = BitmapFactory.decodeFile(String.valueOf(bitmap));
//        int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
//        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
        String base64String = ImageUtil.convert(bitmap);
        //user.setImage(base64String);
        ApiInterface apiInterface = ApiClint.getApiClint();
        Call<RegStatus> call = apiInterface.getImage(loginData.getId(),base64String);

        call.enqueue(new Callback<RegStatus>() {
            @Override
            public void onResponse(Call<RegStatus> call, Response<RegStatus> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ProfileDashboard.this, "Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegStatus> call, Throwable t) {
                Toast.makeText(ProfileDashboard.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        });

        //UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
    }


    private void ShowPasswordDIalog() {

        Button btnCancle,btnSubmit;
        final EditText editTextNew,editTextOld;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.password_change_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCancle = dialog.findViewById(R.id.dialogBtnCancle);
        btnSubmit = dialog.findViewById(R.id.dialogBtnSubmit);
        editTextOld = dialog.findViewById(R.id.edtOldPassword);
        editTextNew = dialog.findViewById(R.id.edtNewPassword);


        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = loginData.getPassword();
                String oldp = editTextOld.getText().toString().trim();
                String newp = editTextNew.getText().toString().trim();
                if (password.equals(oldp)){
                    //user.setPassword(newp);
                    ApiInterface apiInterface = ApiClint.getApiClint();
                    Call<RegStatus> call = apiInterface.getPassword(loginData.getId(),newp);
                    call.enqueue(new Callback<RegStatus>() {
                        @Override
                        public void onResponse(Call<RegStatus> call, Response<RegStatus> response) {
                            if (response.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(ProfileDashboard.this, "Your Password is updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegStatus> call, Throwable t) {
                                String msg = t.getMessage();
                            Toast.makeText(ProfileDashboard.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    editTextNew.setError("Your password Doesn't matched");
                }

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    private void ShowNameDialog() {

        Button btnCancle,btnSubmit;
        final EditText editTextName;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.name_change_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCancle = dialog.findViewById(R.id.dialogBtnCancle);
        btnSubmit = dialog.findViewById(R.id.dialogBtnSubmit);
        editTextName = dialog.findViewById(R.id.edtChangeName);
        editTextName.setText(loginData.getName());

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                if (name.isEmpty()){
                    editTextName.setError("Field can't be empty");
                }else if (!name.matches("^[A-Za-z]+$")){
                    editTextName.setError("Name must be valid");
                }else {
                    editTextName.setError(null);
                    ApiInterface apiInterface = ApiClint.getApiClint();
                    Call<RegStatus> call = apiInterface.getName(loginData.getId(),name);
                    call.enqueue(new Callback<RegStatus>() {
                        @Override
                        public void onResponse(Call<RegStatus> call, Response<RegStatus> response) {
                            if (response.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(ProfileDashboard.this, "Your Name is updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegStatus> call, Throwable t) {
                            String msg = t.getMessage();
                            Toast.makeText(ProfileDashboard.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private void ShowEmailDialog() {

        Button btnCancle,btnSubmit;
        final EditText editTextEmail;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.email_change_poppup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCancle = dialog.findViewById(R.id.dialogBtnCancle);
        btnSubmit = dialog.findViewById(R.id.dialogBtnSubmit);
        editTextEmail = dialog.findViewById(R.id.edtChangeEmail);
        editTextEmail.setText(loginData.getEmail());

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = editTextEmail.getText().toString();
                if (email.isEmpty()){
                    editTextEmail.setError("Filed can't be empty");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Email must be valid ");
                }else {
                    editTextEmail.setError(null);
                    ApiInterface apiInterface = ApiClint.getApiClint();
                    Call<RegStatus> call = apiInterface.getEmail(loginData.getId(),email);
                    call.enqueue(new Callback<RegStatus>() {
                        @Override
                        public void onResponse(Call<RegStatus> call, Response<RegStatus> response) {
                            if (response.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(ProfileDashboard.this, "Your Password is updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegStatus> call, Throwable t) {
                            String msg = t.getMessage();
                            Toast.makeText(ProfileDashboard.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    private void ShowPhoneDialog() {

        Button btnCancle,btnSubmit;
        final EditText editTextPhone;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.phone_change_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCancle = dialog.findViewById(R.id.dialogBtnCancle);
        btnSubmit = dialog.findViewById(R.id.dialogBtnSubmit);
        editTextPhone = dialog.findViewById(R.id.edtChangePhone);
        editTextPhone.setText(loginData.getPhone());

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                if (phone.isEmpty()){
                    editTextPhone.setError("Phone number is required");
                }else if (phone.length()<11){
                    editTextPhone.setError("Valid Phone number is required");
                }else {
                    editTextPhone.setError(null);
                    ApiInterface apiInterface = ApiClint.getApiClint();
                    Call<RegStatus> call = apiInterface.getPhone(loginData.getId(),phone);
                    call.enqueue(new Callback<RegStatus>() {
                        @Override
                        public void onResponse(Call<RegStatus> call, Response<RegStatus> response) {
                            if (response.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(ProfileDashboard.this, "Your Number is updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegStatus> call, Throwable t) {
                            String msg = t.getMessage();
                            Toast.makeText(ProfileDashboard.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


}

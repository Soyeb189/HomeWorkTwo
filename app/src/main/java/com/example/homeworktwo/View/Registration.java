package com.example.homeworktwo.View;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homeworktwo.MainActivity;
import com.example.homeworktwo.Model.LoginData;
import com.example.homeworktwo.Model.RegStatus;
import com.example.homeworktwo.Model.User;
import com.example.homeworktwo.R;
import com.example.homeworktwo.UserDAO;
import com.example.homeworktwo.UserDataBase;
import com.example.homeworktwo.ViewModel.ApiClint;
import com.example.homeworktwo.ViewModel.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {

    private EditText editTextName,editTextEmail,editTextPhone,editTextPassword,editTextConfirmPassword;
    private Button buttonReg;
    private UserDAO dao;
    private LoginData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextName = findViewById(R.id.edtRegName);
        editTextEmail = findViewById(R.id.edtRegEmail);
        editTextPhone = findViewById(R.id.edtRegPhone);
        editTextPassword = findViewById(R.id.edtRegPassword);
        editTextConfirmPassword = findViewById(R.id.edtRegConfirmPassword);

        buttonReg = findViewById(R.id.btnRegister);

//        dao = Room.databaseBuilder(getApplicationContext(), UserDataBase.class,"UserDb")
//                .allowMainThreadQueries()
//                .build().getUserDao();

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidateName() | !ValidatePhone() | !ValidateEmail() | !ValidatePassword() | !ValidateConfirmPassword()){
                    return;
                }

                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                String image = "image";
                String uuid = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                String checked_value = "0";

//                user = UserDataBase.getDataBase(getApplicationContext()).getUserDao().getSelectedUser(email);

                ApiInterface apiInterface2 = ApiClint.getApiClint();
                Call<LoginData> call2 = apiInterface2.getCheckedMail(email);

                call2.enqueue(new Callback<LoginData>() {
                    @Override
                    public void onResponse(Call<LoginData> call2, Response<LoginData> response2) {
                        user = response2.body();
                        if (user!=null){
                            if (password.equals(confirmPassword)){

                                ApiInterface apiInterface = ApiClint.getApiClint();
                                Call<RegStatus> call = apiInterface.getReg(checked_value,email,image,name,password,phone,uuid);
                                call.enqueue(new Callback<RegStatus>() {
                                    @Override
                                    public void onResponse(Call<RegStatus> call, Response<RegStatus> response) {
                                        if (response.isSuccessful()){
                                            if (response.body().getCode().equals("1")){
                                                Toast.makeText(Registration.this, "Success", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(Registration.this, "Response failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<RegStatus> call, Throwable t) {
                                        String message = t.getMessage();
                                        Toast.makeText(Registration.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Intent moveToLogin = new Intent(Registration.this, MainActivity.class);
                                startActivity(moveToLogin);
                                finish();
                            }else {
                                Toast.makeText(Registration.this, "Password is not match", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            editTextEmail.setError("Email already exists");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginData> call, Throwable t) {

                    }

                });


            }
        });
    }

    private boolean ValidateEmail(){
        String emailInput = editTextEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            editTextEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            editTextEmail.setError("Please enter a valid email address");
            return false;
        } else {
            editTextEmail.setError(null);
            return true;
        }
    }

    private boolean ValidateName(){
        String userName = editTextName.getText().toString();
        if (userName.isEmpty()){
            editTextName.setError("Field Can't be empty");
            return false;
        }else if (!userName.matches("^[A-Za-z]+$")){
            editTextName.setError("Valid Name is required");
            return false;
        }else {
            editTextName.setError(null);
            return true;
        }
    }

    private boolean ValidatePassword(){
        String password = editTextPassword.getText().toString();
        if (password.isEmpty()){
            editTextPassword.setError("Field Can't be empty");
            return false;
        }else if (password.length()<6){
            editTextPassword.setError("Length must be more than 6 ");
            return false;
        }else {
            editTextPassword.setError(null);
            return true;
        }
    }

    private boolean ValidateConfirmPassword(){
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (confirmPassword.isEmpty()){
            editTextConfirmPassword.setError("Field Can't be empty");
            return false;
        }else if(!confirmPassword.equals(password)){
            editTextConfirmPassword.setError("Password is not matched");
            return false;
        }else {
            editTextConfirmPassword.setError(null);
            return true;
        }
    }

    private boolean ValidatePhone(){
        String phone = editTextPhone.getText().toString().trim();
        if (phone.isEmpty()){
            editTextPhone.setError("Phone number is required");
            return false;
        }else {
            editTextPhone.setError(null);
            return true;
        }
    }
}

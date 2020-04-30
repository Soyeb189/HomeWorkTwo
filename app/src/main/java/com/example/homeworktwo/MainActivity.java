package com.example.homeworktwo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homeworktwo.Model.LoginData;
import com.example.homeworktwo.Model.User;
import com.example.homeworktwo.View.AdminPanel;
import com.example.homeworktwo.View.ProfileDashboard;
import com.example.homeworktwo.View.Registration;
import com.example.homeworktwo.ViewModel.ApiClint;
import com.example.homeworktwo.ViewModel.ApiInterface;
import com.example.homeworktwo.ViewModel.InternetConnection;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button buttonLogin,buttonReg;
    private EditText editTextEmail,editTextPassword;
    private CheckBox checkBoxRemember;
    private UserDAO dao;
    private UserDataBase dataBase;
    private LoginData user;
    private int m;
    private String uuid = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = findViewById(R.id.btnLogin);
        buttonReg = findViewById(R.id.btnReg);
        editTextEmail = findViewById(R.id.edtLoginEmail);
        editTextPassword = findViewById(R.id.edtLoginPassword);
        checkBoxRemember = findViewById(R.id.checkboxReMe);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home Work");
        setSupportActionBar(toolbar);




        uuid = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
//        user = UserDataBase.getDataBase(getApplicationContext()).getUserDao().getCheckedVal(uuid,1);


        //Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();


        if (user != null){
            checkBoxRemember.setChecked(true);
            editTextEmail.setText(user.getEmail());
            editTextPassword.setText(user.getPassword());
            //Toast.makeText(this, user.getUuid(), Toast.LENGTH_SHORT).show();

        }else {
            checkBoxRemember.setChecked(false);
        }

        if (checkBoxRemember.isChecked()){
            m = 1;
        }else {
            m = 0;
        }


        checkBoxRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxRemember.isChecked()){
                    m = 1;
//                    user.setCheckValue(m);
//                    UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
                }
                if (!checkBoxRemember.isChecked()){
                    m = 0;
//                    user.setCheckValue(m);
//                    UserDataBase.getDataBase(getApplicationContext()).getUserDao().updateUser(user);
                }
                // Toast.makeText(MainActivity.this, String.valueOf(m), Toast.LENGTH_SHORT).show();
            }
        });



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidateEmail() | !ValidatePassword()){
                    return;
                }
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (InternetConnection.checkConnection(MainActivity.this)){
                    ApiInterface apiInterface = ApiClint.getApiClint();
                    Call<LoginData> call = apiInterface.getLogin(email,password);
                    call.enqueue(new Callback<LoginData>() {
                        @Override
                        public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                            LoginData loginData = response.body();

                            if(response.body().getName()!=null) {
                                if (response.body().getEmail().equals("admin@admin.com")){
                                    Intent i = new Intent(MainActivity.this, AdminPanel.class);
                                    startActivity(i);
                                }else {
                                    Intent i = new Intent(MainActivity.this, ProfileDashboard.class);
                                    i.putExtra("User", (Serializable) loginData);
                                    startActivity(i);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<LoginData> call, Throwable t) {
                            String message = t.getMessage();
                            Toast.makeText(MainActivity.this, "Paaword is no matxch", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }



//                user = dao.getUser(email,password);
//                user = UserDataBase.getDataBase(getApplicationContext()).getUserDao().getUser(email,password);



            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Registration.class);
                startActivity(i);
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

    private boolean ValidatePassword(){
        String passwordInput = editTextPassword.getText().toString().trim();
        if (passwordInput.isEmpty()){
            editTextPassword.setError("Field can't be empty");
            return false;
        }else {
            editTextPassword.setError(null);
            return true;
        }

    }
}

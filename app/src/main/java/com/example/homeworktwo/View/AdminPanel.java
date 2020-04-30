package com.example.homeworktwo.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.homeworktwo.Model.AllUser;
import com.example.homeworktwo.Model.LoginData;
import com.example.homeworktwo.Model.User;
import com.example.homeworktwo.Model.UserAdapter;
import com.example.homeworktwo.R;
import com.example.homeworktwo.ViewModel.ApiClint;
import com.example.homeworktwo.ViewModel.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPanel extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<AllUser> users;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        recyclerView = findViewById(R.id.recvwUser);

        CallApi();
    }

    private void CallApi() {
        users = new ArrayList<>();
        ApiInterface apiInterface = ApiClint.getApiClint();
        Call<List<AllUser>> call = apiInterface.getUser();

        call.enqueue(new Callback<List<AllUser>>() {
            @Override
            public void onResponse(Call<List<AllUser>> call, Response<List<AllUser>> response) {


                if (response.isSuccessful()){
                    users = response.body();
                    Toast.makeText(AdminPanel.this,String.valueOf( users.size()), Toast.LENGTH_SHORT).show();

                    ShowOnRecyclerView();
                }else {
                    Toast.makeText(AdminPanel.this, "no", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AllUser>> call, Throwable t) {
                Toast.makeText(AdminPanel.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ShowOnRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        userAdapter = new UserAdapter(users,this);
        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }
}

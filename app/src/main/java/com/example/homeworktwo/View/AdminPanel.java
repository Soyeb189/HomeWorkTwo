package com.example.homeworktwo.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeworktwo.Model.AllUser;
import com.example.homeworktwo.Model.UserAdapter;
import com.example.homeworktwo.R;
import com.example.homeworktwo.ViewModel.ApiClint;
import com.example.homeworktwo.ViewModel.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPanel extends AppCompatActivity implements View.OnLongClickListener {

    private RecyclerView recyclerView;
    private List<AllUser> users;
    private UserAdapter userAdapter;
    private Toolbar toolbar;
    private TextView textViewToolbar;
    public boolean is_in_action_mode = false;
    public List<AllUser> selection_list = new ArrayList<>();
    public int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        toolbar = findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recvwUser);
        textViewToolbar = findViewById(R.id.counter_text);
        textViewToolbar.setVisibility(View.GONE);

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
                    //Toast.makeText(AdminPanel.this,String.valueOf( users.size()), Toast.LENGTH_SHORT).show();

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.admin_menu,menu);
//        return true;
//    }

    @Override
    public boolean onLongClick(View view) {
        CallApi();
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.admin_menu);
        textViewToolbar.setVisibility(View.VISIBLE);
        is_in_action_mode = true;
        userAdapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    public void prepareSelection(View view, int position){
        if (((CheckBox)view).isChecked()){
            selection_list.add(users.get(position));
            counter = counter + 1;
            updateCounter(counter);
        }else {
            selection_list.remove(users.get(position));
            counter = counter - 1;
            updateCounter(counter);
        }
    }

    public void updateCounter(int counter){
        if (counter==0){
            textViewToolbar.setText("0 item selected");
        }else {
            textViewToolbar.setText(counter+ " item selected");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_delete){
            userAdapter.updateAdapter(selection_list);
            //Toast.makeText(this, selection_list.get(2).getEmail(), Toast.LENGTH_SHORT).show();
            clearActionMode();
        }else if (item.getItemId() == android.R.id.home){
            clearActionMode();
            userAdapter.notifyDataSetChanged();
        }
        return true;
    }

    public void clearActionMode(){
        is_in_action_mode = false;
        toolbar.getMenu().clear();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        textViewToolbar.setVisibility(View.GONE);
        textViewToolbar.setText("O item selected");
        counter = 0;
        selection_list.clear();
    }

    @Override
    public void onBackPressed() {
        if (is_in_action_mode){
            clearActionMode();
            userAdapter.notifyDataSetChanged();
        }else {
            super.onBackPressed();
        }

    }
}

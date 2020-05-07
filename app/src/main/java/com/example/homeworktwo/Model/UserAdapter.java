package com.example.homeworktwo.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeworktwo.R;
import com.example.homeworktwo.View.AdminPanel;
import com.example.homeworktwo.ViewModel.ApiClint;
import com.example.homeworktwo.ViewModel.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    public Context context;
    public List<AllUser> users;
    AdminPanel adminPanel;
    public int i;
    public List<AllUser> selection_list = new ArrayList<>();
    public List<Integer> allId = new ArrayList<>();

    public UserAdapter(List<AllUser> users,Context context){
        this.context = context;
        this.users = users;
        adminPanel = (AdminPanel) context;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.userName.setText(users.get(position).getName());
        String name = users.get(position).getName();
        //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
        holder.userEmail.setText(users.get(position).getEmail());
        if (users.get(position).getImage().equals("image")){
            holder.imageViewUserPro.setBackgroundResource(R.drawable.user_bac);
        }else {
            Bitmap image = ImageUtil.convert(users.get(position).getImage());
            holder.imageViewUserPro.setImageBitmap(image);
        }
        if (!adminPanel.is_in_action_mode){
            holder.checkBox.setVisibility(View.GONE);
        }else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
        }
        holder.imageViewUserPro.setOnLongClickListener(adminPanel);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminPanel.prepareSelection(view,i);

                if (((CheckBox)view).isChecked()){
                    allId.add(users.get(position).getId());
                    Toast.makeText(context, String.valueOf(allId) , Toast.LENGTH_SHORT).show();

                }else {
                    allId.remove(users.get(position).getId());
                    Toast.makeText(context, String.valueOf(allId) , Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViewUserPro;
        TextView userName,userEmail;
        AdminPanel adminPanel;
        CheckBox checkBox;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUserPro = itemView.findViewById(R.id.imgUser);
            userName = itemView.findViewById(R.id.tvUserNameA);
            userEmail = itemView.findViewById(R.id.tvUserEmailA);
            checkBox = itemView.findViewById(R.id.check_list_item);

            userName.setOnLongClickListener(adminPanel);

            checkBox.setOnClickListener(this);
            adminPanel = new AdminPanel();
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(context, "HHH", Toast.LENGTH_SHORT).show();
             i = getAdapterPosition();
            Toast.makeText(context, String.valueOf(i), Toast.LENGTH_SHORT).show();
            String name = users.get(i).getName();
            Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            prepareSelection(view,i);
        }
    }

    public void updateAdapter(List<AllUser> list){
        Toast.makeText(context, String.valueOf(allId), Toast.LENGTH_SHORT).show();
        callApi();
        for(AllUser allUser : list){
            users.remove(allUser);
        }
        notifyDataSetChanged();
    }

    private void callApi() {
        ApiInterface apiInterface = ApiClint.getApiClint();

        Call<RegStatus> call = apiInterface.getDeletedStatus(allId);
        call.enqueue(new Callback<RegStatus>() {
            @Override
            public void onResponse(Call<RegStatus> call, Response<RegStatus> response) {
                if (response.isSuccessful()){
                    Toast.makeText(adminPanel, "Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegStatus> call, Throwable t) {
                Toast.makeText(adminPanel, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void prepareSelection(View view, int position){
        if (((CheckBox)view).isChecked()){
            selection_list.add(users.get(position));

        }else {
            selection_list.remove(users.get(position));

        }
    }
}

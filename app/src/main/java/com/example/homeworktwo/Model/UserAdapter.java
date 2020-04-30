package com.example.homeworktwo.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeworktwo.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    public Context context;
    public List<AllUser> users;

    public UserAdapter(List<AllUser> users,Context context){
        this.context = context;
        this.users = users;
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
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
        holder.userEmail.setText(users.get(position).getEmail());
        if (users.get(position).getImage().equals("image")){
            holder.imageViewUserPro.setBackgroundResource(R.drawable.user_bac);
        }else {
            Bitmap image = ImageUtil.convert(users.get(position).getImage());
            holder.imageViewUserPro.setImageBitmap(image);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewUserPro;
        TextView userName,userEmail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUserPro = itemView.findViewById(R.id.imgUser);
            userName = itemView.findViewById(R.id.tvUserNameA);
            userEmail = itemView.findViewById(R.id.tvUserEmailA);
        }
    }
}

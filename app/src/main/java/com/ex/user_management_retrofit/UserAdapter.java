package com.ex.user_management_retrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ex.user_management_retrofit.ModelResponse.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList=userList;

    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        holder.userName.setText(userList.get(position).getUsername());
        holder.userEmail.setText(userList.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName,userEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.etusername);
            userEmail=itemView.findViewById(R.id.etuseremail);
        }
    }
}

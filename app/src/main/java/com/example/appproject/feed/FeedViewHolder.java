package com.example.appproject.feed;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.example.appproject.model.user.User;
import com.example.appproject.model.user.UsersListLoadingState;
import com.squareup.picasso.Picasso;

public class FeedViewHolder extends RecyclerView.ViewHolder {

    ImageView profilePic;
    TextView nameTv;
    TextView locationTv;

    public FeedViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.feedrow_txt_username);
        locationTv = itemView.findViewById(R.id.feedrow_txt_location);
        profilePic = itemView.findViewById(R.id.feedrow_img_user);
        itemView.setOnClickListener(v->{
            int pos = getAdapterPosition();
            listener.onItemClick(v,pos);
        });
    }

    public void bind(User user) {
        nameTv.setText(user.getName());
        locationTv.setText(user.getAddress());
        if(user.getProfilePicUrl() != null){
            if(user.getGender().equals("Female")){
                Picasso.get().load(user.getProfilePicUrl()).placeholder(R.drawable.female_avatar).into(profilePic);
            }
            else{
                Picasso.get().load(user.getProfilePicUrl()).placeholder(R.drawable.avatar).into(profilePic);
            }
        }
        else{
            if(user.getGender().equals("Female")){
                profilePic.setImageResource(R.drawable.female_avatar);
            }
            else{
                profilePic.setImageResource(R.drawable.avatar);
            }
        }

    }
}

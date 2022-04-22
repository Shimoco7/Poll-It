package com.example.appproject.feed;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.R;
import com.example.appproject.model.listeners.OnItemClickListener;
import com.example.appproject.model.user.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FeedViewHolder extends RecyclerView.ViewHolder {

    ImageView profilePic;
    TextView nameTv;
    TextView locationTv;
    ProgressBar progressBar;

    public FeedViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.feedrow_txt_username);
        locationTv = itemView.findViewById(R.id.feedrow_txt_location);
        profilePic = itemView.findViewById(R.id.imgpoll_image);
        progressBar = itemView.findViewById(R.id.feedrow_progressBar);
        progressBar.setVisibility(View.GONE);
        itemView.setOnClickListener(v->{
            int pos = getAdapterPosition();
            listener.onItemClick(v,pos);
        });
    }

    public void bind(User user) {
        nameTv.setText(user.getName());
        locationTv.setText(user.getAddress());
        if(user.getProfilePicUrl() != null){
            progressBar.setVisibility(View.VISIBLE);
                Picasso.get().load(user.getProfilePicUrl()).into(profilePic, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
        }
        else{
            if(user.getGender()!=null){
                if(user.getGender().equals("Female")){
                    profilePic.setImageResource(R.drawable.female_avatar);
                }
                else{
                    profilePic.setImageResource(R.drawable.avatar);
                }
            }
        }

    }
}

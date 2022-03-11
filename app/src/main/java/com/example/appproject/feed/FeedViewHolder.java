package com.example.appproject.feed;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.R;
import com.example.appproject.model.user.User;

public class FeedViewHolder extends RecyclerView.ViewHolder {

    ImageView profilePic;
    TextView nameTv;
    TextView locationTv;

    public FeedViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.feedrow_txt_username);
        locationTv = itemView.findViewById(R.id.feedrow_txt_location);
        itemView.setOnClickListener(v->{
            int pos = getAdapterPosition();
            listener.onItemClick(v,pos);
        });
    }

    public void bind(User user) {
        nameTv.setText(user.getFullName());
        locationTv.setText(user.getLocation());
    }
}

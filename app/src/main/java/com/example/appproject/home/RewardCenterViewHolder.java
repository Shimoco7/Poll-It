package com.example.appproject.home;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.listeners.OnItemClickListener;
import com.example.appproject.model.reward.Reward;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RewardCenterViewHolder extends RecyclerView.ViewHolder {

    ShapeableImageView prizeImage;
    ShapeableImageView clickImage;
    MaterialTextView prizeName;
    MaterialTextView price;

    public RewardCenterViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        prizeImage = itemView.findViewById(R.id.prizerow_image);
        clickImage = itemView.findViewById(R.id.prizerow_click_image);
        prizeName = itemView.findViewById(R.id.prizerow_txt_prizename);
        price = itemView.findViewById(R.id.prizerow_txt_price);
        clickImage.setOnClickListener(v->{
            int pos = getAdapterPosition();
            onItemClickListener.onItemClick(v,pos);
        });
    }

    public void bind(Reward reward) {
        price.setText(String.valueOf(reward.getPrice()));
        prizeName.setText(reward.getTitle());
        if(reward.getImage() != null){
            General.loadImage(reward.getImage(),prizeImage,R.drawable.loadimagebig);
        }
    }
}

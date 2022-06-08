package com.example.appproject.home;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.listeners.OnItemClickListener;
import com.example.appproject.model.poll.Poll;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HomeViewHolder extends RecyclerView.ViewHolder{

    MaterialTextView pollsName;
    MaterialCardView pollCard;
    ShapeableImageView pollIcon,pollMainImage;

    public HomeViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        pollsName = itemView.findViewById(R.id.homescr_poll_pollName);
        pollCard = itemView.findViewById(R.id.homescr_btn_poll);
        pollIcon = itemView.findViewById(R.id.homescr_poll_icon);
        pollMainImage = itemView.findViewById(R.id.homescr_poll_mainImage);

        itemView.setOnClickListener(v->{
            int pos = getAdapterPosition();
            listener.onItemClick(v,pos);
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void bind(Poll poll) {
        pollMainImage.setImageResource(R.drawable.default_poll_image);
        pollsName.setText(poll.getPollName());
        pollMainImage.setAlpha((float)1);
        pollsName.setAlpha((float)1);
        pollIcon.setImageDrawable(MyApplication.getContext().getResources().getDrawable(R.drawable.ic_feed_arrow));
        if(poll.getImage() != null){
            General.loadImage(poll.getImage(),pollMainImage,R.drawable.loadimagebig,isSuccessful->{
                if(!isSuccessful){
                    pollMainImage.setImageResource(R.drawable.default_poll_image);
                }
            });
        }
    }
}

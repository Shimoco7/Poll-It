package com.example.appproject.poll;

import android.content.res.ColorStateList;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.listeners.OnItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;

public class PollQuestionImageAnswersViewHolder extends RecyclerView.ViewHolder{

    ShapeableImageView option;
    String url;

    public PollQuestionImageAnswersViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        option = itemView.findViewById(R.id.image_q_square_image);
        option.setOnClickListener(v->{
            option.setAlpha((float)1.0);
            int pos = getAdapterPosition();
            listener.onItemClick(v,pos);
        });
    }

    public void bind(String url) {
        this.url = url;
        General.loadImage(url,option,R.drawable.loadimagebig,bool -> {});
    }

    public void bind(String url, boolean isTheAnswer) {
        this.url = url;
        General.loadImage(url,option,R.drawable.loadimagebig,bool -> {});
        if(isTheAnswer){
            option.setAlpha((float)1.0);
        }
        else{
            this.option.setAlpha((float)0.25);
        }
    }
}

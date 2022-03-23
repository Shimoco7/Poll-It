package com.example.appproject.home;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.feed.OnItemClickListener;
import com.example.appproject.model.Model;
import com.example.appproject.model.poll.Poll;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class HomeViewHolder extends RecyclerView.ViewHolder{

    MaterialTextView pollsName;
    MaterialCardView pollCard;
    ShapeableImageView pollIcon;

    public HomeViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        pollsName = itemView.findViewById(R.id.poll_list_square_poll_name);
        pollCard = itemView.findViewById(R.id.homescr_btn_poll);
        pollIcon = itemView.findViewById(R.id.homescr_poll_icon);

        itemView.setOnClickListener(v->{
            int pos = getAdapterPosition();
            listener.onItemClick(v,pos);
        });
    }

    public void bind(Poll poll) {
        pollsName.setText(poll.getPollName());
        Model.instance.isPollFilled(MyApplication.getUserKey(),poll.getPollId(),isFilled->{
            if(isFilled){

            }
            else{

            }
        });

    }
}

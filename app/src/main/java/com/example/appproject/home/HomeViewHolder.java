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
    ShapeableImageView pollIcon,pollMainImage,pollDoneImage;

    public HomeViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        pollsName = itemView.findViewById(R.id.homescr_poll_pollName);
        pollCard = itemView.findViewById(R.id.homescr_btn_poll);
        pollIcon = itemView.findViewById(R.id.homescr_poll_icon);
        pollMainImage = itemView.findViewById(R.id.homescr_poll_mainImage);
        pollDoneImage = itemView.findViewById(R.id.homescr_poll_doneImage);


        itemView.setOnClickListener(v->{
            int pos = getAdapterPosition();
            listener.onItemClick(v,pos);
        });
    }

    public void bind(Poll poll) {
        pollsName.setText(poll.getPollName());
        Model.instance.isPollFilled(MyApplication.getUserKey(),poll.getPollId(),isFilled->{
            if(isFilled){
                Model.instance.getMainThread().post(()->{
                    pollMainImage.setAlpha((float)0.4);
                    pollsName.setAlpha((float)0.4);
                    pollDoneImage.setVisibility(View.VISIBLE);
                    pollIcon.setImageDrawable(MyApplication.getContext().getResources().getDrawable(R.drawable.ic_edit));
                });
            }
        });

    }
}

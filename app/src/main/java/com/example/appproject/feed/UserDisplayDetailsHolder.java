package com.example.appproject.feed;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.R;
import com.example.appproject.model.poll.Poll;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class UserDisplayDetailsHolder extends RecyclerView.ViewHolder {

    MaterialTextView pollsName;
    ShapeableImageView icon;

    public UserDisplayDetailsHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        pollsName = itemView.findViewById(R.id.homescr_poll_pollName);
        icon=itemView.findViewById(R.id.homescr_poll_icon);
        itemView.setOnClickListener(v->{
            int pos = getAdapterPosition();
            onItemClickListener.onItemClick(v,pos);
        });
    }

    public void bind(Poll poll) {
//        icon.setVisibility(View.INVISIBLE);
        pollsName.setText(poll.getPollName());

    }
}

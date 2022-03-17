package com.example.appproject.home;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.R;
import com.example.appproject.feed.OnItemClickListener;
import com.example.appproject.model.poll.Poll;
import com.google.android.material.textview.MaterialTextView;

public class HomeViewHolder extends RecyclerView.ViewHolder{

    MaterialTextView pollsName;

    public HomeViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        pollsName = itemView.findViewById(R.id.poll_list_square_poll_name);
        itemView.setOnClickListener(v->{
            int pos = getAdapterPosition();
            listener.onItemClick(v,pos);
        });
    }

    public void bind(Poll poll) {
        pollsName.setText(poll.getPollName());
    }
}

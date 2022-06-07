package com.example.appproject.displaydetails;

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
    UserDisplayDetailsViewModel viewModel;
    public UserDisplayDetailsHolder(@NonNull View itemView,UserDisplayDetailsViewModel viewModel) {
        super(itemView);
        this.viewModel = viewModel;
        pollsName = itemView.findViewById(R.id.homescr_poll_pollName);
        icon=itemView.findViewById(R.id.homescr_poll_icon);
    }

    public void bind(Poll poll) {
        pollsName.setText(poll.getPollName());
        icon.setOnClickListener(v->{
        });
    }
}

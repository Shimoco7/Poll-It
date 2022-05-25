package com.example.appproject.poll;

import android.content.res.ColorStateList;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.listeners.OnItemClickListener;
import com.google.android.material.button.MaterialButton;

public class PollQuestionMultiChoiceViewHolder extends RecyclerView.ViewHolder {

    MaterialButton option;

    public PollQuestionMultiChoiceViewHolder(@NonNull View itemView, OnItemClickListener listener){
        super(itemView);
        option = itemView.findViewById(R.id.poll_ans_row);
        option.setOnClickListener(v->{
            option.setBackgroundTintList(ColorStateList.valueOf(MyApplication.getContext().getResources().getColor(R.color.primeGreen)));
            option.setAlpha(1);
            int pos = getAdapterPosition();
            listener.onItemClick(v,pos);
        });
    }

    public void bind(String option) {
        this.option.setText(option);
    }

    public void bind(String option, boolean isTheAnswer) {
        this.option.setText(option);
        if(isTheAnswer){
            this.option.setBackgroundTintList(ColorStateList.valueOf(MyApplication.getContext().getColor(R.color.primeGreen)));
            this.option.setAlpha(1);
        }
        else{
            this.option.setBackgroundTintList(ColorStateList.valueOf(MyApplication.getContext().getColor(R.color.primeGray)));
            this.option.setAlpha((float)0.25);
        }
    }
}

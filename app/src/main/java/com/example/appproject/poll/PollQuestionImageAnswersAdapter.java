package com.example.appproject.poll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.listeners.OnItemClickListener;

import java.util.Objects;

public class PollQuestionImageAnswersAdapter extends RecyclerView.Adapter<PollQuestionImageAnswersViewHolder>{

    PollQuestionImageAnswersViewModel viewModel;
    LayoutInflater layoutInflater;
    OnItemClickListener onItemClickListener;


    public PollQuestionImageAnswersAdapter(PollQuestionImageAnswersViewModel viewModel, LayoutInflater layoutInflater) {
        this.viewModel = viewModel;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public PollQuestionImageAnswersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) MyApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_question_square,parent,false);
        return new PollQuestionImageAnswersViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PollQuestionImageAnswersViewHolder holder, int position) {
        String option = Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getChoices().get(position);
        holder.bind(option);
    }

    @Override
    public int getItemCount() {
        if(viewModel.getPollQuestionWithAnswer() == null || viewModel.getPollQuestionWithAnswer().getValue() == null){
            return 0;
        }
        return Objects.requireNonNull(viewModel.getPollQuestionWithAnswer().getValue()).pollQuestion.getChoices().size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

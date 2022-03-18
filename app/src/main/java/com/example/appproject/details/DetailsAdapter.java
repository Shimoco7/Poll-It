package com.example.appproject.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.question.Question;

import java.util.Objects;
interface OnItemClickListener{
    void onItemClick(int position);
}
public class DetailsAdapter extends RecyclerView.Adapter<DetailsHolder>{

    DetailsViewModel detailsViewModel;
    LayoutInflater layoutInflater;
    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public DetailsAdapter(DetailsViewModel feedViewModel,LayoutInflater layoutInflater) {
        this.detailsViewModel = feedViewModel;
        this.layoutInflater = layoutInflater;
    }


    @NonNull
    @Override
    public DetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) MyApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.details_list_row,parent,false);
        return new DetailsHolder(view, detailsViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsHolder holder, int position) {
        Question question = Objects.requireNonNull(detailsViewModel.getQuestions().getValue()).get(position);
        holder.bind(question);
    }

    @Override
    public int getItemCount() {
        if(detailsViewModel.getQuestions().getValue() == null){
            return 0;
        }
        return detailsViewModel.getQuestions().getValue().size();
    }
}

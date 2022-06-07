package com.example.appproject.displaydetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.poll.Poll;

import java.util.Objects;

public class UserDisplayDetailsAdapter extends RecyclerView.Adapter<UserDisplayDetailsHolder> {

    UserDisplayDetailsViewModel viewModel;
    LayoutInflater layoutInflater;

    public UserDisplayDetailsAdapter(UserDisplayDetailsViewModel viewModel, LayoutInflater layoutInflater) {
        this.viewModel = viewModel;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public UserDisplayDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) MyApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.poll_list_square,parent,false);
        return new UserDisplayDetailsHolder(view,viewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDisplayDetailsHolder holder, int position) {
        Poll poll = Objects.requireNonNull(viewModel.getUserFilledPolls().get(position));
        holder.bind(poll);
    }

    @Override
    public int getItemCount() {
        if(viewModel.getUserFilledPolls() == null){
            return 0;
        }
        return viewModel.getUserFilledPolls().size();
    }

}

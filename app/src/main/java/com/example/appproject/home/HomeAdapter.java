package com.example.appproject.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.feed.OnItemClickListener;
import com.example.appproject.model.poll.Poll;

import java.util.Objects;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder>{

    HomeViewModel homeViewModel;
    LayoutInflater layoutInflater;
    OnItemClickListener onItemClickListener;

    public HomeAdapter(HomeViewModel homeViewModel,LayoutInflater layoutInflater) {
        this.homeViewModel = homeViewModel;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) MyApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.poll_list_square,parent,false);
        return new HomeViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Poll poll = Objects.requireNonNull(homeViewModel.getPolls().getValue()).get(position);
        holder.bind(poll);
    }

    @Override
    public int getItemCount() {
        if(homeViewModel.getPolls().getValue() == null){
            return 0;
        }
        return homeViewModel.getPolls().getValue().size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

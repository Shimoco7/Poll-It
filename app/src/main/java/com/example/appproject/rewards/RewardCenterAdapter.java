package com.example.appproject.rewards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.listeners.OnItemClickListener;
import com.example.appproject.model.reward.Reward;

import java.util.Objects;

public class RewardCenterAdapter extends RecyclerView.Adapter<RewardCenterViewHolder> {

    RewardCenterViewModel viewModel;
    LayoutInflater layoutInflater;
    OnItemClickListener onItemClickListener;

    public RewardCenterAdapter(RewardCenterViewModel viewModel, LayoutInflater layoutInflater) {
        this.viewModel = viewModel;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public RewardCenterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) MyApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.prize_line,parent,false);
        return new RewardCenterViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardCenterViewHolder holder, int position) {
        Reward reward = Objects.requireNonNull(viewModel.getRewards().getValue()).get(position);
        holder.bind(reward);
    }

    @Override
    public int getItemCount() {
        if(viewModel.getRewards().getValue() == null){
            return 0;
        }
        return viewModel.getRewards().getValue().size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

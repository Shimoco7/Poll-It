package com.example.appproject.rewards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.reward.Order;

import java.util.Objects;

public class UserOrdersAdapter extends RecyclerView.Adapter<UserOrdersViewHolder> {

    UserOrdersViewModel viewModel;
    LayoutInflater layoutInflater;

    public UserOrdersAdapter(UserOrdersViewModel viewModel, LayoutInflater layoutInflater) {
        this.viewModel = viewModel;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public UserOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) MyApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.user_prize_row,parent,false);
        return new UserOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrdersViewHolder holder, int position) {
        Order order = Objects.requireNonNull(viewModel.getOrders().getValue()).get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        if(viewModel.getOrders().getValue() == null){
            return 0;
        }
        return viewModel.getOrders().getValue().size();
    }
}

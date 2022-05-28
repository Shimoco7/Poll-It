package com.example.appproject.rewards;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrdersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

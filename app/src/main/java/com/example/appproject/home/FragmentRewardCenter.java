package com.example.appproject.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class FragmentRewardCenter extends Fragment {

    RewardCenterViewModel viewModel;
    RewardCenterAdapter adapter;
    MaterialTextView totalCoins;
    RecyclerView list;
    SwipeRefreshLayout swipeRefresh;

    public FragmentRewardCenter() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(requireActivity()).get(RewardCenterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward_center, container, false);
        totalCoins = view.findViewById(R.id.rewardCenter_text_coinsNumber);
        swipeRefresh = view.findViewById(R.id.rewardCenter_layout_refresh);
        list = view.findViewById(R.id.rewardCenter_rv);

        totalCoins.setText(MyApplication.getUserCoins());
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RewardCenterAdapter(viewModel,getLayoutInflater());
        list.setAdapter(adapter);

        viewModel.getRewards().observe(getViewLifecycleOwner(),rewards -> refresh());
        Model.instance.refreshRewards();
        adapter.setOnItemClickListener((v,pos)->{
            String rewardId = Objects.requireNonNull(viewModel.getRewards().getValue()).get(pos).getId();
            Navigation.findNavController(v).navigate(FragmentRewardCenterDirections.actionFragmentRewardCenterToFragmentPrize(rewardId));
        });
        swipeRefresh.setOnRefreshListener(Model.instance::refreshRewards);
        observeRewardsLoadingState();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    private void observeRewardsLoadingState() {
        Model.instance.getRewardsListLoadingState().observe(getViewLifecycleOwner(),usersListLoadingState ->{
            switch (usersListLoadingState){
                case loading:
                    swipeRefresh.setRefreshing(true);
                    break;
                case loaded:
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        });
    }
}
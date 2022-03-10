package com.example.appproject.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appproject.R;
import com.example.appproject.model.Model;

public class FragmentFeed extends Fragment {

    FeedViewModel feedViewModel;
    FeedAdapter feedAdapter;
    RecyclerView list;
    SwipeRefreshLayout swipeRefresh;

    public FragmentFeed() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        swipeRefresh = view.findViewById(R.id.feed_layout_refresh);
        list = view.findViewById(R.id.feed_rv);

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        feedAdapter = new FeedAdapter(feedViewModel,getLayoutInflater());
        list.setAdapter(feedAdapter);

        swipeRefresh.setOnRefreshListener(Model.instance::refreshList);
        feedViewModel.getUsers().observe(getViewLifecycleOwner(),usersList->refresh());
        observeUserLoadingState();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        feedAdapter.notifyDataSetChanged();
    }

    private void observeUserLoadingState() {
        Model.instance.getUsersListLoadingState().observe(getViewLifecycleOwner(),usersListLoadingState ->{
            switch (usersListLoadingState){
                case loading:
                    swipeRefresh.setRefreshing(true);
                    break;
                case loaded:
                    swipeRefresh.setRefreshing(false);
                    break;
                //TODO - handle error
                case error:
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        });
    }
}
package com.example.appproject.feed;

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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.google.android.material.button.MaterialButton;

public class FragmentFeed extends Fragment {

    FeedViewModel feedViewModel;
    FeedAdapter feedAdapter;
    RecyclerView list;
    SwipeRefreshLayout swipeRefresh;

    public FragmentFeed() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.main_menu_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

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
        MaterialButton mapBtn = view.findViewById(R.id.feed_btn_map);
        MaterialButton backBtn = view.findViewById(R.id.feed_back_btn);

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        feedAdapter = new FeedAdapter(feedViewModel,getLayoutInflater());
        list.setAdapter(feedAdapter);
        feedAdapter.setOnItemClickListener((v,pos)->{
            String userId = feedViewModel.getUsers().getValue().get(pos).getUid();
            Navigation.findNavController(v).navigate(FragmentFeedDirections.actionFragmentFeedToFragmentUserDisplayDetails(userId));
        });
        backBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigateUp();
        });
        mapBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentFeedDirections.actionGlobalFragmentMap()));
        swipeRefresh.setOnRefreshListener(()->{
            Model.instance.refreshList();
            Model.instance.refreshPollsList();
        });
        feedViewModel.getUsers().observe(getViewLifecycleOwner(),usersList->refresh());
        observeUserLoadingState();
        Model.instance.refreshList();
        Model.instance.refreshPollsList();

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
            }
        });
    }
}
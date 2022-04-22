package com.example.appproject.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.Model;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;


public class FragmentHomeScreen extends Fragment {

    HomeViewModel homeViewModel;
    HomeAdapter homeAdapter;
    RecyclerView list;
    SwipeRefreshLayout swipeRefresh;

    public FragmentHomeScreen() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        swipeRefresh = view.findViewById(R.id.home_layout_poll_refresh);
        list = view.findViewById(R.id.home_poll_rv);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MaterialTextView userName = view.findViewById(R.id.homeScr_text_name);
        userName.setText(MyApplication.getUserName());

        list.setHasFixedSize(true);
        int numOfColumns = 2;
        list.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns,GridLayoutManager.VERTICAL,false));
        homeAdapter = new HomeAdapter(homeViewModel,getLayoutInflater());
        list.setAdapter(homeAdapter);
        list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.set(5,20,5,20);
            }
        });

        homeViewModel.getPolls().observe(getViewLifecycleOwner(),usersList->refresh());
        Model.instance.refreshPollsList();
        homeAdapter.setOnItemClickListener((v,pos)->{
            String pollId = Objects.requireNonNull(homeViewModel.getPolls().getValue()).get(pos).getPollId();
            Navigation.findNavController(v).navigate(FragmentHomeScreenDirections.actionFragmentHomeScreenToFragmentActivePoll(pollId));
        });

        swipeRefresh.setOnRefreshListener(Model.instance::refreshPollsList);
        observePollsLoadingState();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        homeAdapter.notifyDataSetChanged();
    }

    private void observePollsLoadingState() {
        Model.instance.getPollsListLoadingState().observe(getViewLifecycleOwner(),usersListLoadingState ->{
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
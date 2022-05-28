package com.example.appproject.rewards;

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
import android.widget.Button;

import com.example.appproject.R;


public class FragmentUserOrders extends Fragment {

    RecyclerView list;
    UserOrdersAdapter adapter;
    UserOrdersViewModel viewModel;
    SwipeRefreshLayout swipeRefresh;

    public FragmentUserOrders() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(requireActivity()).get(UserOrdersViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_orders, container, false);
        swipeRefresh = view.findViewById(R.id.userOrders_layout_refresh);
        list = view.findViewById(R.id.userOrders_rv);
        Button homeBtn = view.findViewById(R.id.userOrders_home_btn);

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserOrdersAdapter(viewModel,getLayoutInflater());
        list.setAdapter(adapter);


        homeBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentUserOrdersDirections.actionGlobalFragmentHomeScreen()));
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        adapter.notifyDataSetChanged();
    }


}
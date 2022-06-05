package com.example.appproject.rewards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import com.example.appproject.model.Model;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class FragmentUserOrders extends Fragment {

    RecyclerView list;
    UserOrdersAdapter adapter;
    UserOrdersViewModel viewModel;
    SwipeRefreshLayout swipeRefresh;
    KonfettiView konfetti;
    private Model instance;

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
        konfetti = view.findViewById(R.id.userOrders_konfetti);
        boolean afterPurchase = FragmentUserOrdersArgs.fromBundle(getArguments()).getPurchased();

        if (afterPurchase) konfettiBuild(konfetti);




        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserOrdersAdapter(viewModel,getLayoutInflater());
        list.setAdapter(adapter);

        viewModel.getOrders().observe(getViewLifecycleOwner(),orders -> refresh());
        Model.instance.refreshOrders();

        homeBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentUserOrdersDirections.actionGlobalFragmentHomeScreen()));
        swipeRefresh.setOnRefreshListener(Model.instance::refreshOrders);
        observeOrdersLoadingState();
        if(afterPurchase){
            konfettiBuild(konfetti);
        }
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    private void observeOrdersLoadingState() {
        Model.instance.getOrdersListLoadingState().observe(getViewLifecycleOwner(),ordersListLoadingState ->{
            switch (ordersListLoadingState){
                case loading:
                    swipeRefresh.setRefreshing(true);
                    break;
                case loaded:
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        });
    }

    private void konfettiBuild(KonfettiView konfetti) {
        konfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(8, 4f))
                .setPosition(-50f, 800f, -50f, -50f)
                .streamFor(300, 5000L);

        Model.instance.getMainThread().post(() ->
        {
            Snackbar.make(requireView(),"Congratulations !, The purchase was successful", 5000)
                    .setBackgroundTint(requireContext().getColor(R.color.primeOrng))
                    .setTextColor(requireContext().getColor(R.color.white))
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setAnchorView(list)
                    .show();

        });
    }


}
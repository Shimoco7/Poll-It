package com.example.appproject.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject.MainActivity;
import com.example.appproject.R;
import com.example.appproject.model.Model;

public class FragmentUserDetails extends Fragment {
    Button finishBtn;
    DetailsViewModel detailsViewModel;
    DetailsAdapter detailsAdapter;
    RecyclerView list;

    public FragmentUserDetails() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        list = view.findViewById(R.id.details_list_rv);

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsAdapter = new DetailsAdapter(detailsViewModel,getLayoutInflater());
        list.setAdapter(detailsAdapter);
        detailsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG","row was clicked " + position);
                String id = detailsViewModel.getDetails().get(position).getQuestion();

            }
        });
        finishBtn = view.findViewById(R.id.userDetails_next_btn);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        finishBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        return view;
    }


}
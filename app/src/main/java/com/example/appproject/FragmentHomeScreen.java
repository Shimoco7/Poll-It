package com.example.appproject;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;

import org.w3c.dom.Text;

public class FragmentHomeScreen extends Fragment {


    public FragmentHomeScreen() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        CardView pollBtn = view.findViewById(R.id.homescr_btn_poll);
        Button feedBtn = view.findViewById(R.id.homescr_btn_feed);
        Button mapsBtn = view.findViewById(R.id.homescr_btn_map);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MaterialTextView userName = view.findViewById(R.id.homeScr_text_name);
        userName.setText(MyApplication.getUserName());

        pollBtn.setOnClickListener(Navigation.createNavigateOnClickListener((FragmentHomeScreenDirections.actionFragmentHomeScreenToFragmentActivePoll())));
        feedBtn.setOnClickListener(Navigation.createNavigateOnClickListener((FragmentHomeScreenDirections.actionFragmentHomeScreenToFragmentFeed())));
        mapsBtn.setOnClickListener(Navigation.createNavigateOnClickListener((FragmentHomeScreenDirections.actionGlobalFragmentMap())));

        return view;

    }
}
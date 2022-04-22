package com.example.appproject.home;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.Model;


public class FragmentPrize extends Fragment {


    public FragmentPrize() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prize, container, false);
        Button collectBtn=view.findViewById(R.id.prize_btn_collect);
        collectBtn.setOnClickListener((v -> {
            popup();
        }));
        return view;
    }

    private void popup(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage("Are You Sure?")
                .setPositiveButton("BUY", (dialog, which) -> {
                    //Navigation.findNavController(v).navigate(FragmentHomeScreenDirections.actionFragmentHomeScreenToFragmentActivePoll(pollId));
                })
                .setNeutralButton("Cancel", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }
}
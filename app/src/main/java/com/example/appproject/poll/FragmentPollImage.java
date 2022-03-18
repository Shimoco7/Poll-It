package com.example.appproject.poll;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject.R;
import com.google.android.material.button.MaterialButton;


public class FragmentPollImage extends Fragment {

    MaterialButton backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poll_image, container, false);

        backBtn.setOnClickListener(v->{
            Navigation.findNavController(v).navigateUp();
        });
        return view;
    }
}
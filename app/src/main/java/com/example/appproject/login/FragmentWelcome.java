package com.example.appproject.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject.R;


public class FragmentWelcome extends Fragment {


    public FragmentWelcome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        Button signInBtn = view.findViewById(R.id.welcome_sign_in_btn);
        Button registerBtn = view.findViewById(R.id.welcome_register_btn);
        signInBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentWelcomeDirections.actionFragmentWelcomeToFragmentSignIn()));
        registerBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentWelcomeDirections.actionFragmentWelcomeToFragmentRegister()));

        return view;
    }
}
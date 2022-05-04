package com.example.appproject.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appproject.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;


public class FragmentWelcome extends Fragment {

    private static final String EMAIL = "email";

    public FragmentWelcome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList(EMAIL));
        loginButton.setFragment(this);
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("TAG", exception.getLocalizedMessage());
            }
        });
        Button signInBtn = view.findViewById(R.id.welcome_sign_in_btn);
        Button registerBtn = view.findViewById(R.id.welcome_register_btn);
        signInBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentWelcomeDirections.actionFragmentWelcomeToFragmentSignIn()));
        registerBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentWelcomeDirections.actionFragmentWelcomeToFragmentRegister()));

        return view;
    }
}
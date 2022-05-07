package com.example.appproject.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;


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
        loginButton.setPermissions(Collections.singletonList(EMAIL));
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", loginResult.getAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (jsonObject, graphResponse) -> {
                    //TODO - get AccessToken, Id, Name and Email
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                //TODO - handle on cancel to Login via Facebook
            }

            @Override
            public void onError(FacebookException exception) {
                //TODO - handle on Error to Login via Facebook
            }
        });
        Button signInBtn = view.findViewById(R.id.welcome_sign_in_btn);
        Button registerBtn = view.findViewById(R.id.welcome_register_btn);
        signInBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentWelcomeDirections.actionFragmentWelcomeToFragmentSignIn()));
        registerBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentWelcomeDirections.actionFragmentWelcomeToFragmentRegister()));

        return view;
    }
}
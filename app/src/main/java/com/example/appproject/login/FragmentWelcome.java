package com.example.appproject.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.appproject.MainActivity;
import com.example.appproject.R;
import com.example.appproject.model.General;
import com.example.appproject.model.Model;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class FragmentWelcome extends Fragment {

    private static final String EMAIL = "email";
    ProgressBar progressBar;

    public FragmentWelcome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        progressBar = view.findViewById(R.id.welcome_progressbar);
        progressBar.setVisibility(View.GONE);


        CallbackManager callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setPermissions(Collections.singletonList(EMAIL));
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                General.progressBarOn(getActivity(),container,progressBar,false);
                Log.d("TAG", loginResult.getAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (jsonObject, graphResponse) -> {
                    try {
                        String email = (String) jsonObject.get("email");
                        String id = (String) jsonObject.get("id");
                        String name = (String) jsonObject.get("name");
                        String profilePicUrl = null;
                        if(!jsonObject.isNull("picture")){
                            JSONObject picture = (JSONObject) jsonObject.get("picture");
                            if(!picture.isNull("data")){
                                JSONObject data = (JSONObject) picture.get("data");
                                if(!picture.isNull("data")){
                                    profilePicUrl = (String) data.get("url");
                                }
                            }
                        }
                    Model.instance.facebookLogin(email,id,name,profilePicUrl,(user,message)->{
                        if (user != null) {
                            if(message.equals(getString(R.string.success))){
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                requireActivity().finish();
                            }
                            else if(message.equals(getString(R.string.registration_details_needed))){
                                Navigation.findNavController(requireView()).navigate(R.id.fragmentUserDetails);
                            }
                        }
                        else {
                            if(message.equals(getString(R.string.account_unreliability_rank_too_high))){
                                Snackbar.make(requireView(), getString(R.string.user_has_been_blocked), 8000)
                                        .setBackgroundTint(requireContext().getColor(R.color.primeRed))
                                        .setTextColor(requireContext().getColor(R.color.white))
                                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                                        .show();
                            }
                            else{
                                Snackbar.make(requireView(),getString(R.string.server_is_off),5000).setAction("", view->{ }).show();
                            }
                            General.progressBarOff(getActivity(),container,progressBar,false);
                            LoginManager.getInstance().logOut();
                        }
                    });

                    } catch (JSONException e) {
                        General.progressBarOff(getActivity(),container,progressBar,false);
                        LoginManager.getInstance().logOut();
                        Snackbar.make(requireView(),"Facebook Login Error",3000).setAction("",view->{ }).show();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Snackbar.make(requireView(),"Facebook Login Canceled",3000).setAction("",view->{ }).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Snackbar.make(requireView(),"Facebook Login Error",3000).setAction("",view->{ }).show();
            }
        });
        Button signInBtn = view.findViewById(R.id.welcome_sign_in_btn);
        Button registerBtn = view.findViewById(R.id.welcome_register_btn);
        signInBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentWelcomeDirections.actionFragmentWelcomeToFragmentSignIn()));
        registerBtn.setOnClickListener(Navigation.createNavigateOnClickListener(FragmentWelcomeDirections.actionFragmentWelcomeToFragmentRegister()));

        return view;
    }
}
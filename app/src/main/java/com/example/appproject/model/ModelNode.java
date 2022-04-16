package com.example.appproject.model;

import android.content.Context;
import android.util.Log;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.user.BooleanListener;
import com.example.appproject.model.user.LoginResult;
import com.example.appproject.model.user.RegisterResult;
import com.example.appproject.model.user.User;
import com.example.appproject.model.user.UserListener;
import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelNode {

    private final Retrofit retrofit;
    private final ModelMethodsInterface methodsInterface;
    private final Context appContext = MyApplication.getContext();
    private final String BASE_URL = "http://10.0.2.2:3000";

    public ModelNode() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        methodsInterface = retrofit.create(ModelMethodsInterface.class);
    }

    /**
    * Authentication
    *
    */
    public void register(String emailAddress, String password, UserListener userListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", emailAddress);
        map.put("password", password);

        Call<Void> call = methodsInterface.register(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Model.instance.login(emailAddress,password,userListener);
                }
                else if(response.code() == 400){
                    try {
                        if(response.errorBody().string().contains("already registered")){
                            Model.instance.getMainThread().post(()->{
                                userListener.onComplete(null, appContext.getString(R.string.user_already_exists));
                            });
                        }
                        else{
                            Model.instance.getMainThread().post(()->{
                                userListener.onComplete(null, appContext.getString(R.string.registration_failed));
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Model.instance.getMainThread().post(()->{
                    userListener.onComplete(null, "Ops, We Ran Into An Issue...");
                });
                Log.d("TAG" , "FAILURE: " + t.getMessage());
            }
        });
    }



    public void login(String emailAddress, String password, UserListener userListener){
        HashMap<String, String> map = new HashMap<>();
        map.put("email", emailAddress);
        map.put("password", password);

        Call<LoginResult> call = methodsInterface.login(map);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if(response.code() == 200){
                    LoginResult loginResult = response.body();
                    assert loginResult != null;
                    User u = loginResult.getAccount();
                    MyApplication.setUserKey(u.getUid());
                    MyApplication.setUserEmail(u.getEmail());
                    MyApplication.setAccessToken(loginResult.getAccessToken());
                    MyApplication.setRefreshToken(loginResult.getRefreshToken());
                    if(loginResult.getDetailsFilled()){
                        MyApplication.setUserName(u.getName());
                        MyApplication.setGender(u.getGender());
                        MyApplication.setUserAddress(u.getAddress());
                        MyApplication.setUserProfilePicUrl(u.getProfilePicUrl());
                        Model.instance.getMainThread().post(()->{
                            userListener.onComplete(u, appContext.getString(R.string.success));
                        });
                    }
                    else{
                        Model.instance.getMainThread().post(()->{
                            userListener.onComplete(u, appContext.getString(R.string.registration_details_needed));
                        });
                    }
                }
                else{
                    Log.d("TAG", "signInWithEmail:failure - " + response.code());
                    Model.instance.getMainThread().post(()->{
                        userListener.onComplete(null, "Response code from server: " + response.code());
                    });
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Log.d("TAG", "signInWithEmail:failure", t.getCause());
                Model.instance.getMainThread().post(()->{
                    userListener.onComplete(null, null);
                });
            }
        });
    }

    public void isSignedIn(BooleanListener booleanListener){
        HashMap<String, String> map = new HashMap<>();
        map.put("refresh_token", MyApplication.getRefreshToken());

        Call<RefreshTokenResult> call = methodsInterface.refreshToken(map);
        call.enqueue(new Callback<RefreshTokenResult>() {
            @Override
            public void onResponse(Call<RefreshTokenResult> call, Response<RefreshTokenResult> response) {
                if(response.code() == 200){
                    RefreshTokenResult tokenResult = response.body();
                    assert  tokenResult != null;
                    MyApplication.setAccessToken(tokenResult.getAccessToken());
                    MyApplication.setRefreshToken(tokenResult.getRefreshToken());
                    booleanListener.onComplete(true);
                }
                else{
                    booleanListener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResult> call, Throwable t) {
                booleanListener.onComplete(false);
            }
        });

    }
}



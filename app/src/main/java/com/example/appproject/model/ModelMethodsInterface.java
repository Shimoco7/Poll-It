package com.example.appproject.model;

import com.example.appproject.model.user.LoginResult;
import com.example.appproject.model.user.RegisterResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ModelMethodsInterface {
    @POST("/auth/register")
    Call<Void> register(@Body HashMap<String,String> map);

    @POST("/auth/login")
    Call<LoginResult> login(@Body HashMap<String,String> map);

    @POST("/auth/refreshToken")
    Call<RefreshTokenResult> refreshToken(@Body HashMap<String,String> map);
}

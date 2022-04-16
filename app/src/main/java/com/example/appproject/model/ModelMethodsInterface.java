package com.example.appproject.model;

import com.example.appproject.model.question.Question;
import com.example.appproject.model.user.LoginResult;
import com.example.appproject.model.user.RegisterResult;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ModelMethodsInterface {
    /**
     *
     *  Authentication
     *
     */

    @POST("/auth/register")
    Call<Void> register(@Body HashMap<String,String> map);

    @POST("/auth/login")
    Call<LoginResult> login(@Body HashMap<String,String> map);

    @POST("/auth/refreshToken")
    Call<RefreshTokenResult> refreshToken(@Body HashMap<String,String> map);

    @DELETE("/auth/logout")
    Call<Void> logout(@Body HashMap<String,String> map);

    /**
     *
     *  User Details
     *
     */

    @GET("question/getAllQuestions")
    Call<List<Question>> getAllQuestions(@Header("Authorization") String accessToken);
}

package com.example.appproject.model;

import com.example.appproject.model.question.Question;
import com.example.appproject.model.user.LoginResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Call<Void> register(@Body Map<String,String> map);

    @POST("/auth/login")
    Call<LoginResult> login(@Body Map<String,String> map);

    @POST("/auth/refreshToken")
    Call<RefreshTokenResult> refreshToken(@Body Map<String,String> map);

    @POST("/auth/logout")
    Call<Void> logout(@Body Map<String,String> map);

    /**
     *
     *  User Details
     *
     */

    @POST("/auth/update")
    Call<Void> updateUser(@Header("Authorization") String accessToken,@Body Map<String,String> map);

    @GET("question/getAllQuestions")
    Call<List<Question>> getAllQuestions(@Header("Authorization") String accessToken);

    @POST("/detail/create")
    Call<Void> createDetail(@Header("Authorization") String accessToken,@Body Map<String,String> map);
}

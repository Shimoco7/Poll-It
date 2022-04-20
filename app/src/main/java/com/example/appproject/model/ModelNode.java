package com.example.appproject.model;

import android.content.Context;
import android.util.Log;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.listeners.VoidListener;
import com.example.appproject.model.question.GetQuestionsListener;
import com.example.appproject.model.question.Question;
import com.example.appproject.model.listeners.BooleanListener;
import com.example.appproject.model.user.LoginResult;
import com.example.appproject.model.user.User;
import com.example.appproject.model.user.UserListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelNode {

    private final Retrofit retrofit;
    private final ModelMethodsInterface methodsInterface;
    private final Context appContext = MyApplication.getContext();
    private final String BASE_URL_EMULATOR_LOCAL = "http://10.0.2.2:3000";
    private final String BASE_URL_SERVER = "http://10.10.248.124:8000";

    public ModelNode() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        methodsInterface = retrofit.create(ModelMethodsInterface.class);
    }

    /**
    *
     *  Authentication
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
                Log.e("TAG" , "Register FAILURE: " + t.getMessage());
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
                    Log.e("TAG", "signInWithEmail:failure - " + response.code());
                    Model.instance.getMainThread().post(()->{
                        userListener.onComplete(null, "Response code from server: " + response.code());
                    });
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Log.e("TAG", "signInWithEmail:failure", t.getCause());
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
                    assert tokenResult != null;
                    MyApplication.setAccessToken(tokenResult.getAccessToken());
                    MyApplication.setRefreshToken(tokenResult.getRefreshToken());
                    Log.d("TAG","refresh token: " + tokenResult.getRefreshToken());
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

    public void signOut(VoidListener listener){
        HashMap<String, String> map = new HashMap<>();
        map.put("refresh_token", MyApplication.getRefreshToken());

        Call<Void> call = methodsInterface.logout(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    listener.onComplete();
                }
                else{
                    listener.onComplete();
                    Log.e("TAG" , "Logout FAILURE: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG" , "Logout FAILURE: " + t.getMessage());
                listener.onComplete();
            }
        });
    }


    /**
     *
     *  User Details
     *
     */

    public void updateUser(String userId, Map<String,String> map, VoidListener listener){
        map.put("_id",userId);
        Call<Void> call = methodsInterface.updateUser("Bearer "+ MyApplication.getAccessToken(),map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    listener.onComplete();
                }
                else{
                    listener.onComplete();
                    Log.e("TAG" , "Update user FAILURE: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG" , "Logout FAILURE: " + t.getMessage());
                listener.onComplete();
            }
        });
    }

    public void getQuestions(GetQuestionsListener listener){
        Call<List<Question>> call = methodsInterface.getAllQuestions("Bearer "+ MyApplication.getAccessToken());
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if(response.code() == 200){
                    List<Question> questions = response.body();
                    assert questions != null;
                    listener.onComplete(questions);
                }
                else{
                    Log.d("TAG", "getAllQuestions FAILURE: " + response.code());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.d("TAG" , "getAllQuestions FAILURE: " + t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void saveDetailToDb(Detail detail, VoidListener listener){
        Map<String,String> map = detail.toJson();
        Call<Void> call = methodsInterface.createDetail("Bearer "+ MyApplication.getAccessToken(),map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    listener.onComplete();
                }
                else{
                    listener.onComplete();
                    Log.e("TAG" , "Create detail FAILURE: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG" , "Create detail FAILURE: " + t.getMessage());
                listener.onComplete();
            }
        });
    }


}



package com.example.appproject.model;

import android.content.Context;
import android.util.Log;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.listeners.GetDetailsListener;
import com.example.appproject.model.listeners.GetPollQuestionsListener;
import com.example.appproject.model.listeners.GetPollsListener;
import com.example.appproject.model.listeners.GetRewardsListener;
import com.example.appproject.model.listeners.GetUserListener;
import com.example.appproject.model.listeners.SaveImageListener;
import com.example.appproject.model.listeners.VoidListener;
import com.example.appproject.model.listeners.GetQuestionsListener;
import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.poll.Poll;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.model.question.Question;
import com.example.appproject.model.listeners.BooleanListener;
import com.example.appproject.model.reward.Reward;
import com.example.appproject.model.user.LoginResult;
import com.example.appproject.model.user.User;
import com.example.appproject.model.listeners.LoginListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private final String BASE_URL_SERVER = "https://poll-it.cs.colman.ac.il";

    public ModelNode() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(General.getOkHttpClient())
                .build();
        methodsInterface = retrofit.create(ModelMethodsInterface.class);
    }

    /**
    *
     *  Authentication
    *
    */
    public void register(String emailAddress, String password, LoginListener loginListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", emailAddress);
        map.put("password", password);

        Call<Void> call = methodsInterface.register(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Model.instance.login(emailAddress,password, loginListener);
                }
                else if(response.code() == 400){
                    try {
                        if(response.errorBody().string().contains("already registered")){
                            Model.instance.getMainThread().post(()-> loginListener.onComplete(null, appContext.getString(R.string.user_already_exists)));
                        }
                        else{
                            Model.instance.getMainThread().post(()-> loginListener.onComplete(null, appContext.getString(R.string.registration_failed)));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Model.instance.getMainThread().post(()-> loginListener.onComplete(null, "Ops, We Ran Into An Issue..."));
                Log.e("TAG" , "Register FAILURE: " + t.getMessage());
            }
        });
    }


    public void login(String emailAddress, String password, LoginListener loginListener){
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
                        Model.instance.getMainThread().post(()-> loginListener.onComplete(u, appContext.getString(R.string.success)));
                    }
                    else{
                        Model.instance.getMainThread().post(()-> loginListener.onComplete(u, appContext.getString(R.string.registration_details_needed)));
                    }
                }
                else{
                    Log.e("TAG", "signInWithEmail:failure - " + response.code());
                    Model.instance.getMainThread().post(()->{
                        loginListener.onComplete(null, "Response code from server: " + response.code());
                    });
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Log.e("TAG", "signInWithEmail:failure", t.getCause());
                Model.instance.getMainThread().post(()->{
                    loginListener.onComplete(null, null);
                });
            }
        });
    }


    public void facebookLogin(String emailAddress, String id,String name,String profilePicUrl, LoginListener loginListener){
        HashMap<String, String> map = new HashMap<>();
        map.put("email", emailAddress);
        map.put("facebookId", id);
        map.put("name", name);
        if(profilePicUrl != null && profilePicUrl.length() > 0){
            map.put("profilePicUrl", profilePicUrl);
        }

        Call<LoginResult> call = methodsInterface.facebookLogin(map);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if(response.code() == 200){
                    LoginResult loginResult = response.body();
                    assert loginResult != null;
                    User u = loginResult.getAccount();
                    MyApplication.setUserKey(u.getUid());
                    MyApplication.setUserEmail(u.getEmail());
                    MyApplication.setUserName(u.getName());
                    MyApplication.setFacebookId(u.getFacebookId());
                    MyApplication.setUserProfilePicUrl(u.getProfilePicUrl());
                    MyApplication.setAccessToken(loginResult.getAccessToken());
                    MyApplication.setRefreshToken(loginResult.getRefreshToken());
                    if(loginResult.getDetailsFilled()){
                        MyApplication.setGender(u.getGender());
                        MyApplication.setUserAddress(u.getAddress());
                        Model.instance.getMainThread().post(()-> loginListener.onComplete(u, appContext.getString(R.string.success)));
                    }
                    else{
                        Model.instance.getMainThread().post(()-> loginListener.onComplete(u, appContext.getString(R.string.registration_details_needed)));
                    }
                }
                else{
                    Log.e("TAG", "facebook sign in via server ERROR: " + response.code());
                    Model.instance.getMainThread().post(()->{
                        loginListener.onComplete(null, "Response code from server: " + response.code());
                    });
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Log.e("TAG", "facebook sign in via server FAILED:", t.getCause());
                Model.instance.getMainThread().post(()->{
                    loginListener.onComplete(null, null);
                });
            }
        });
    }



    public void isSignedIn(BooleanListener booleanListener){
        HashMap<String, String> map = new HashMap<>();
        map.put("refreshToken", MyApplication.getRefreshToken());

        Call<RefreshTokenResult> call = methodsInterface.refreshToken(map);
        call.enqueue(new Callback<RefreshTokenResult>() {
            @Override
            public void onResponse(Call<RefreshTokenResult> call, Response<RefreshTokenResult> response) {
                if(response.code() == 200){
                    RefreshTokenResult tokenResult = response.body();
                    assert tokenResult != null;
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

    public void updatePassword(String oldPass,String newPass,BooleanListener listener){
        HashMap<String, String> map = new HashMap<>();
        map.put("_id", MyApplication.getUserKey());
        map.put("oldPassword", oldPass);
        map.put("newPassword", newPass);

        Call<Void> call = methodsInterface.updatePassword("Bearer "+ MyApplication.getAccessToken(),map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    listener.onComplete(true);
                }
                else{
                    Log.e("TAG" , "Update Password Error: " + response.code());
                    listener.onComplete(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG" , "Update Password FAILURE: " + t.getMessage());
                listener.onComplete(false);
            }
        });
    }

    public void signOut(VoidListener listener){
        HashMap<String, String> map = new HashMap<>();
        map.put("refreshToken", MyApplication.getRefreshToken());

        Call<Void> call = methodsInterface.logout(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200) {
                    Log.w("TAG", "Logout Warning: " + response.code());
                }
                listener.onComplete();
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

    public void updateUser(String userId, Map<String,Object> map, LoginListener listener){
        map.put("_id",userId);
        Call<User> call = methodsInterface.updateUser("Bearer "+ MyApplication.getAccessToken(),map);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    User user = response.body();
                    listener.onComplete(user, appContext.getString(R.string.success));
                }
                else{
                    Log.e("TAG" , "Update user FAILURE: " + response.code());
                    listener.onComplete(null,null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG" , "Logout FAILURE: " + t.getMessage());
                listener.onComplete(null,null);
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
                    Log.e("TAG", "getAllQuestions FAILURE: " + response.code());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("TAG" , "getAllQuestions FAILURE: " + t.getMessage());
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
                if (response.code() != 200) {
                    Log.e("TAG", "Create detail FAILURE: " + response.code());
                }
                listener.onComplete();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG" , "Create detail FAILURE: " + t.getMessage());
                listener.onComplete();
            }
        });
    }


    public void getDetailsByUserId(String userKey, GetDetailsListener listener) {
        Call<List<Detail>> call = methodsInterface.getDetailsByUserId("Bearer "+ MyApplication.getAccessToken(),userKey);
        call.enqueue(new Callback<List<Detail>>() {
            @Override
            public void onResponse(Call<List<Detail>> call, Response<List<Detail>> response) {
                if(response.code() == 200){
                    List<Detail> details = response.body();
                    listener.onComplete(details);
                }
                else{
                    Log.e("TAG" , "Get Details by User Id FAILED: " + response.code());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<List<Detail>> call, Throwable t) {
                Log.e("TAG" , "Get Details by User Id FAILED: " + t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void saveImage(File image, SaveImageListener listener){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),image);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",image.getName(),requestFile);
        Call<UploadImageResult> call = methodsInterface.uploadImage("Bearer "+ MyApplication.getAccessToken(),body);
        call.enqueue(new Callback<UploadImageResult>() {
            @Override
            public void onResponse(Call<UploadImageResult> call, Response<UploadImageResult> response) {
                if(response.code() == 200){
                    UploadImageResult result = response.body();
                    assert result != null;
                    if(!image.delete()){
                        Log.w("TAG" , "Delete Image FAILED");
                    }
                    listener.onComplete(result.getUrl());
                }
                else{
                    Log.e("TAG" , "Save Image FAILED: " + response.code());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<UploadImageResult> call, Throwable t) {
                Log.e("TAG" , "Save Image FAILED: " + t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    /**
     *
     *  Polls
     *
     */

    public void getPolls(GetPollsListener listener) {
        Call<List<Poll>> call = methodsInterface.getPolls("Bearer "+ MyApplication.getAccessToken(),MyApplication.getUserKey());
        call.enqueue(new Callback<List<Poll>>() {
            @Override
            public void onResponse(Call<List<Poll>> call, Response<List<Poll>> response) {
                if(response.code() == 200){
                    List<Poll> polls = response.body();
                    assert polls != null;
                    listener.onComplete(polls);
                }
                else{
                    Log.e("TAG", "getPolls FAILURE: " + response.code());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<List<Poll>> call, Throwable t) {
                Log.e("TAG" , "getPolls FAILURE: " + t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void getPollQuestionsByPollId(String pollId, GetPollQuestionsListener listener) {
        Call<List<PollQuestion>> call = methodsInterface.getPollQuestionsByPollId("Bearer "+ MyApplication.getAccessToken(),pollId);
        call.enqueue(new Callback<List<PollQuestion>>() {
            @Override
            public void onResponse(Call<List<PollQuestion>> call, Response<List<PollQuestion>> response) {
                if(response.code() == 200){
                    List<PollQuestion> pollQuestions = response.body();
                    listener.onComplete(pollQuestions);
                }
                else{
                    Log.e("TAG" , "get PollQuestions By PollId FAILED: " + response.code());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<List<PollQuestion>> call, Throwable t) {
                Log.e("TAG" , "get PollQuestions By PollId FAILED: " + t.getMessage());
                listener.onComplete(null);
            }
        });
    }


    public void saveAnswerToDb(Answer answer, VoidListener listener) {
        Map<String,String> map = answer.toJson();
        Call<Void> call = methodsInterface.saveAnswer("Bearer "+ MyApplication.getAccessToken(),map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200) {
                    Log.e("TAG", "Save Answer FAILURE: " + response.code());
                }
                listener.onComplete();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG" , "Save Answer FAILURE: " + t.getMessage());
                listener.onComplete();
            }
        });
    }

    /**
     *
     *  Rewards
     *
     */

    public void getRewards(GetRewardsListener listener){
        Call<List<Reward>> call = methodsInterface.getAllRewards("Bearer "+ MyApplication.getAccessToken());
        call.enqueue(new Callback<List<Reward>>() {
            @Override
            public void onResponse(Call<List<Reward>> call, Response<List<Reward>> response) {
                if(response.code() == 200){
                    List<Reward> rewards = response.body();
                    assert rewards != null;
                    listener.onComplete(rewards);
                }
                else{
                    Log.e("TAG", "getAllRewards FAILURE: " + response.code());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<List<Reward>> call, Throwable t) {
                Log.e("TAG" , "getAllRewards FAILURE: " + t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void redeemReward(String rewardId, GetUserListener listener){
        Map<String,String> map = new HashMap<>();
        map.put("accountId",MyApplication.getUserKey());
        map.put("rewardId",rewardId);
        Call<User> call = methodsInterface.redeemReward("Bearer "+ MyApplication.getAccessToken(),map);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    listener.onComplete(response.body());
                }
                else{
                    Log.e("TAG", "Redeem Reward FAILURE: " + response.code());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG", "Redeem Reward FAILURE: " + t.getMessage());
                listener.onComplete(null);
            }
        });
    }


}



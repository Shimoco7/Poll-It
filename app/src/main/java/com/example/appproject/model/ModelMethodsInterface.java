package com.example.appproject.model;

import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.poll.Poll;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.model.question.Question;
import com.example.appproject.model.reward.Reward;
import com.example.appproject.model.user.LoginResult;
import com.example.appproject.model.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @POST("/auth/facebook")
    Call<LoginResult> facebookLogin(@Body Map<String,String> map);

    @POST("/auth/refreshToken")
    Call<RefreshTokenResult> refreshToken(@Body Map<String,String> map);

    @PUT("/auth/updatePassword")
    Call<Void> updatePassword(@Header("Authorization") String accessToken, @Body Map<String,String> map);

    @POST("/auth/logout")
    Call<Void> logout(@Body Map<String,String> map);

    /**
     *
     *  User Details
     *
     */

    @PUT("/auth/update")
    Call<User> updateUser(@Header("Authorization") String accessToken, @Body Map<String,Object> map);

    @GET("/detail_question/getAllDetailQuestions")
    Call<List<Question>> getAllQuestions(@Header("Authorization") String accessToken);

    @POST("/detail/create")
    Call<Void> createDetail(@Header("Authorization") String accessToken,@Body Map<String,String> map);

    @GET("/detail/getDetailsByAccountId/{accountId}")
    Call<List<Detail>> getDetailsByUserId(@Header("Authorization") String accessToken, @Path("accountId") String accountId);


    /**
     *
     *  Storage
     *
     */

    @Multipart
    @POST("upload")
    Call<UploadImageResult> uploadImage(@Header("Authorization") String accessToken,@Part MultipartBody.Part file);

    /**
     *
     *  Polls
     *
     */

    @GET("/poll/getPollsByUserId/{accountId}")
    Call<List<Poll>> getPolls(@Header("Authorization") String accessToken,@Path("accountId") String accountId);

    @GET("/poll_question/getPollQuestionsByPollId/{pollId}")
    Call<List<PollQuestion>> getPollQuestionsByPollId(@Header("Authorization") String accessToken, @Path("pollId") String pollId);

    @POST("/answer/create")
    Call<Void> saveAnswer(@Header("Authorization") String accessToken,@Body Map<String,String> map);

    /**
     *
     *  Rewards
     *
     */

    @GET("/reward/getAllRewards")
    Call<List<Reward>> getAllRewards(@Header("Authorization") String accessToken);

    @POST("/reward/redeemReward")
    Call<User> redeemReward(@Header("Authorization") String accessToken,@Body Map<String,String> map);
}

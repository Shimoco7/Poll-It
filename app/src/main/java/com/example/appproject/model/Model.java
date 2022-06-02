package com.example.appproject.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.listeners.DoubleListener;
import com.example.appproject.model.listeners.FileListener;
import com.example.appproject.model.listeners.GetPollListener;
import com.example.appproject.model.listeners.GetPollQuestionWithBooleanListener;
import com.example.appproject.model.listeners.GetRewardListener;
import com.example.appproject.model.listeners.GetUserListener;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.listeners.GetDetailsListener;
import com.example.appproject.model.listeners.GetDetailListener;
import com.example.appproject.model.listeners.IntegerListener;
import com.example.appproject.model.listeners.MapStringToObjListener;
import com.example.appproject.model.listeners.VoidListener;
import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.listeners.GetPollQuestionListener;
import com.example.appproject.model.listeners.GetPollQuestionsListener;
import com.example.appproject.model.listeners.GetPollQuestionWithAnswerListener;
import com.example.appproject.model.listeners.GetPollsListener;
import com.example.appproject.model.poll.Poll;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.model.poll.PollQuestionWithAnswer;
import com.example.appproject.model.poll.PollWithPollQuestionsAndAnswers;
import com.example.appproject.model.poll.LoadingState;
import com.example.appproject.model.question.Question;
import com.example.appproject.model.listeners.BooleanListener;
import com.example.appproject.model.listeners.SaveImageListener;
import com.example.appproject.model.reward.Order;
import com.example.appproject.model.reward.Reward;
import com.example.appproject.model.user.User;
import com.example.appproject.model.listeners.LoginListener;
import com.example.appproject.model.user.UserPollCrossRef;
import com.example.appproject.model.user.UserWithPolls;
import com.example.appproject.model.user.UsersListLoadingState;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Model {

    public static final Model instance = new Model();
    ModelNode modelNode = new ModelNode();
    Executor executor = Executors.newSingleThreadExecutor();
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    private Model(){
        pollsListLoadingState.setValue(LoadingState.loaded);
    }

    public Executor getExecutor() {
        return executor;
    }

    public Handler getMainThread() {
        return mainThread;
    }

    /**
    * Authentication
     *
     */

    public void register(String emailAddress, String password, LoginListener loginListener) {
        modelNode.register(emailAddress,password, loginListener);
    }

    public void login(String emailAddress, String password, LoginListener loginListener) {
        modelNode.login(emailAddress,password, ((user, message) -> {
            if(user != null){
                executor.execute(()-> AppLocalDb.db.userDao().insertAll(user));
                if(message.equals(MyApplication.getContext().getString(R.string.success))){
                    modelNode.getDetailsByUserId(MyApplication.getUserKey(),details->{
                        if(details!=null){
                            for(Detail d : details){
                                executor.execute(()->{
                                    AppLocalDb.db.detailDao().insertAll(d);
                                });
                            }
                        }
                    });
                }
            }
            loginListener.onComplete(user,message);
        }));
    }

    public void facebookLogin(String emailAddress, String id,String name,String profilePicUrl, LoginListener loginListener) {
        modelNode.facebookLogin(emailAddress,id,name,profilePicUrl, ((user, message) -> {
            if(user != null){
                executor.execute(()-> AppLocalDb.db.userDao().insertAll(user));
                if(message.equals(MyApplication.getContext().getString(R.string.success))){
                    modelNode.getDetailsByUserId(MyApplication.getUserKey(),details->{
                        if(details!=null){
                            for(Detail d : details){
                                executor.execute(()->{
                                    AppLocalDb.db.detailDao().insertAll(d);
                                });
                            }
                        }
                    });
                }
            }
            loginListener.onComplete(user,message);
        }));
    }

    public void updatePassword(String oldPass, String newPass,BooleanListener listener){
        modelNode.updatePassword(oldPass,newPass,listener);
    }

    public void isSignedIn(BooleanListener booleanListener){
         modelNode.isSignedIn(isSignedIn->{
             if(isSignedIn){
                if(MyApplication.getFacebookId() != null && MyApplication.getFacebookId().length() > 0){
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                    booleanListener.onComplete(isLoggedIn);
                }
                else{
                    booleanListener.onComplete(true);
                }
             }
             else{
                 booleanListener.onComplete(false);
             }
         });
    }

    public void signOut(VoidListener listener) {
        modelNode.signOut(()->{
            if(MyApplication.getFacebookId() != null && MyApplication.getFacebookId().length() > 0){
                LoginManager.getInstance().logOut();
            }
            Model.instance.clearCaches();
            listener.onComplete();
        });
    }

    public boolean validateEmailAddress(String emailAddress) {
        return EmailValidator.getInstance().isValid(emailAddress);
    }

    public boolean validatePassword(String password) {
        final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,20}$";
        return password.matches(regex);
    }

    public boolean validateName(String name) {
        final String nameRegex = "^[a-zA-Z\\s]{3,15}";
        return name.matches(nameRegex);
    }

    public boolean validateAddress(String address){
        return !address.equals("");
    }

    public void clearCaches() {
        executor.execute(()->{
            if(MyApplication.getFacebookId() != null && MyApplication.getFacebookId().length() > 0){
                LoginManager.getInstance().logOut();
            }
            AppLocalDb.db.clearAllTables();
            MyApplication.getContext().getSharedPreferences("Status",Context.MODE_PRIVATE).edit().clear().apply();
        });
    }

    /**
     * Data - User
     *
     */

    public void getUsersFromLocalDb(BooleanListener listener){
        executor.execute(()-> listener.onComplete(AppLocalDb.db.userDao().getAll().isEmpty()));
    }

    public void updateUser(String userId, Map<String,Object> map, LoginListener listener) {
        modelNode.updateUser(userId,map,(user,message)->{
            if(user != null && message.equals(MyApplication.getContext().getString(R.string.success))){
                executor.execute(()-> AppLocalDb.db.userDao().insertAll(user));
                listener.onComplete(user,message);
            }
            else{
                listener.onComplete(null,null);
            }
        });
    }

    public void getUserRankAndCoins(String userId, MapStringToObjListener listener){
        executor.execute(()->{
            User u = AppLocalDb.db.userDao().loadUserById(userId);
            Map<String,Object> map = new HashMap<>();
            map.put(MyApplication.getContext().getString(R.string.user_coins),u.getCoins());
            map.put(MyApplication.getContext().getString(R.string.user_rank),u.getRank());
            mainThread.post(()->listener.onComplete(map));
        });
    }

    /**
     * Data - User Details
     *
     */

    public void saveDetailOnLocalDb(Detail detail) {
        executor.execute(()-> AppLocalDb.db.detailDao().insertAll(detail));
    }

    public void saveDetailToRemoteDb(Detail detail, VoidListener listener) {
        modelNode.saveDetailToDb(detail, listener);
    }


    public void getUserDetailById(String userKey, String question, GetDetailListener listener) {
        executor.execute(()->{
            Detail detail = AppLocalDb.db.detailDao().loadDetailByUserId(userKey,question);
            mainThread.post(()->listener.onComplete(detail));
        });
    }

    public void updateAnswerByDetailId(String detailId, String answer, VoidListener listener) {
        executor.execute(()->{
            AppLocalDb.db.detailDao().updateAnswerByDetailId(detailId,answer);
            mainThread.post(listener::onComplete);
        });
    }

    public void getAllDetails(String userKey, GetDetailsListener getAllDetailsListener) {
        executor.execute(()->{
            List<Detail> list = AppLocalDb.db.detailDao().getAllDetails(userKey);
            mainThread.post(()->getAllDetailsListener.onComplete(list));
        });
    }

    /**
     * Data - Questions
     */

    MutableLiveData<List<Question>> questionList = new MutableLiveData<>();

    public MutableLiveData<List<Question>> getQuestions() {
        if (questionList == null) {
            refreshQuestions();
        }
        return questionList;
    }

    public void refreshQuestions() {
        modelNode.getQuestions(questions ->
                executor.execute(() -> {
                    for (Question question : questions) {
                        AppLocalDb.db.questionDao().insertAll(question);
                    }
                    questionList.postValue(questions);
                }));
    }


    /**
     * Data - Polls
     *
     */

    MutableLiveData<List<Poll>> pollsList = new MutableLiveData<>();
    MutableLiveData<LoadingState> pollsListLoadingState = new MutableLiveData<>();

    public LiveData<List<Poll>> getPolls() {
        return pollsList;
    }

    public MutableLiveData<LoadingState> getPollsListLoadingState() {
        return pollsListLoadingState;
    }

    public void refreshPollsList() {
        pollsListLoadingState.setValue(LoadingState.loading);
        modelNode.getPolls(polls->{
            if(polls != null){
                for(Poll poll : polls){
                    executor.execute(()->AppLocalDb.db.pollDao().insertAll(poll));
                    getPollQuestionsByPollId(poll.getPollId(), pollQuestions->{
                        if(pollQuestions != null){
                            executor.execute(()->AppLocalDb.db.pollDao().updatePollTotalQuestionsNumberById(poll.getPollId(),pollQuestions.size()));
                            Integer index = 1;
                            for(PollQuestion pollQuestion : pollQuestions){
                                pollQuestion.setQuestionNumber(index);
                                executor.execute(()-> AppLocalDb.db.pollQuestionDao().insertAll(pollQuestion));
                                index++;
                            }
                        }
                    });
                }
            }
            pollsList.postValue(polls);
            pollsListLoadingState.postValue(LoadingState.loaded);
        });
    }

    private void getPollQuestionsByPollId(String pollId, GetPollQuestionsListener listener) {
        modelNode.getPollQuestionsByPollId(pollId,listener);
    }

    public void getPollByPollId(String pollId, GetPollListener listener){
        executor.execute(()->{
            Poll poll = AppLocalDb.db.pollDao().getPollByPollId(pollId);
            mainThread.post(()->listener.onComplete(poll));
        });
    }

    public void getPollQuestionsFromLocalDb(String pollId,GetPollQuestionsListener listener){
        executor.execute(()-> {
            List<PollQuestion> pollQuestions = AppLocalDb.db.pollDao().getPollWithQuestions(pollId).get(0).pollQuestions;
            mainThread.post(()->listener.onComplete(pollQuestions));
        });
    }

    public void getPollQuestionByNumber(String pollId,Integer number, GetPollQuestionListener listener){
        getPollQuestionsFromLocalDb(pollId,pollQuestions->{
            for(PollQuestion p : pollQuestions){
                if(p.getQuestionNumber().equals(number)){
                    listener.onComplete(p);
                }
            }
        });
    }

    public void getLastUnansweredPollQuestion(String pollId, GetPollQuestionWithBooleanListener listener){
        executor.execute(()->{
            List<PollQuestionWithAnswer> pollQuestionWithAnswers = AppLocalDb.db.pollQuestionDao().getPollQuestionsByPollId(pollId);
            for(int i=0;i<pollQuestionWithAnswers.size();i++){
                if(pollQuestionWithAnswers.get(i).answer == null){
                    listener.onComplete(pollQuestionWithAnswers.get(i).pollQuestion, false);
                    return;
                }
            }
            if(pollQuestionWithAnswers.size() == 0){
                listener.onComplete(null,false);
            }
            else{
                listener.onComplete(pollQuestionWithAnswers.get(pollQuestionWithAnswers.size()-1).pollQuestion, pollQuestionWithAnswers.size() == 1);
            }
        });
    }

    public void getPollQuestionWithAnswer(String pollQuestionId, GetPollQuestionWithAnswerListener listener){
        executor.execute(()->{
            PollQuestionWithAnswer pollQuestionWithAnswer = AppLocalDb.db.pollDao().getPollQuestionWithAnswer(pollQuestionId);
            mainThread.post(()->listener.onComplete(pollQuestionWithAnswer));
        });
    }

    public void getPollNumberOfQuestions(String pollId, IntegerListener listener){
        executor.execute(()->{
            Integer numOfQuestions = AppLocalDb.db.pollDao().getPollByPollId(pollId).getTotalNumberOfQuestions();
            mainThread.post(()->listener.onComplete(numOfQuestions));
        });
    }

    public void saveAnswerOnLocalDb(Answer answer){
        executor.execute(()->AppLocalDb.db.answerDao().insertAll(answer));
    }

    public void savePollAnswersToRemoteDb(String userId, String pollId, DoubleListener listener) {
        executor.execute(()->{
            List<PollWithPollQuestionsAndAnswers> pollWithPollQuestionsAndAnswers = AppLocalDb.db.pollDao().getPollWithPollQuestionsAndAnswers(pollId);
            List<PollQuestionWithAnswer> pollQuestionWithAnswer = pollWithPollQuestionsAndAnswers.get(0).pollQuestionWithAnswers;
            Double timeForAllAnswers = 0.0;
            for(PollQuestionWithAnswer pqwa : pollQuestionWithAnswer){
                if(pqwa.answer.getUserId().equals(userId)){
                    timeForAllAnswers += pqwa.answer.getTimeInSeconds();
                    modelNode.saveAnswerToDb(pqwa.answer,()->{});
                }
            }
            Double finalTimeForAllAnswers = timeForAllAnswers;
            mainThread.post(()->listener.onComplete(finalTimeForAllAnswers));
        });
    }

    /**
     * Rewards
     *
     */

    MutableLiveData<List<Reward>> rewardsList = new MutableLiveData<>();
    MutableLiveData<LoadingState> rewardsListLoadingState = new MutableLiveData<>();

    MutableLiveData<List<Order>> ordersList = new MutableLiveData<>();
    MutableLiveData<LoadingState> ordersListLoadingState = new MutableLiveData<>();

    public LiveData<List<Reward>> getRewards() {
        return rewardsList;
    }

    public MutableLiveData<LoadingState> getRewardsListLoadingState() {
        return rewardsListLoadingState;
    }

    public LiveData<List<Order>> getOrders() {
        return ordersList;
    }

    public MutableLiveData<LoadingState> getOrdersListLoadingState() {
        return ordersListLoadingState;
    }

    public void refreshRewards() {
        rewardsListLoadingState.setValue(LoadingState.loading);
        modelNode.getRewards(rewards->{
            executor.execute(()->{
                for(Reward reward : rewards){
                    AppLocalDb.db.rewardDao().insertAll(reward);
                }
                rewardsList.postValue(rewards);
                rewardsListLoadingState.postValue(LoadingState.loaded);
            });
        });
    }

    public void refreshOrders() {
        ordersListLoadingState.setValue(LoadingState.loading);
        executor.execute(()->{
            List<Order> orders = AppLocalDb.db.userDao().loadUserById(MyApplication.getUserKey()).getRewards();
            ordersList.postValue(orders);
            ordersListLoadingState.postValue(LoadingState.loaded);
        });
    }

    public void getRewardFromLocalDb(String rewardId, GetRewardListener listener) {
        executor.execute(()->{
            Reward reward = AppLocalDb.db.rewardDao().getRewardById(rewardId);
            mainThread.post(()->listener.onComplete(reward));
        });
    }

    public void redeemReward(String rewardId,GetUserListener listener){
        modelNode.redeemReward(rewardId,user->{
            if(user != null){
                executor.execute(()-> {
                    AppLocalDb.db.userDao().insertAll(user);
                    mainThread.post(()->listener.onComplete(user));
                });
            }
        });
    }




    /**
     * Storage
     *
     */

    public void saveImage(File file, SaveImageListener listener){
        modelNode.saveImage(file,url -> {
            if (url != null) {
                MyApplication.setUserProfilePicUrl(url);
                HashMap<String,Object> map = new HashMap<>();
                map.put("profilePicUrl",url);
                Model.instance.updateUser(MyApplication.getUserKey(),map,(user,message)->{
                    if(user == null){
                        Log.e("TAG","FAILED TO UPDATE PROFILE PICTURE URL FOR USER: "+MyApplication.getUserKey());
                    }
                });
            }
            listener.onComplete(url);
        });
    }


    public void convertBitmapToFile(Bitmap bitmap, FileListener listener) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File f = new File(MyApplication.getContext().getString(R.string.sd_card_download_path) + MyApplication.getUserKey() + ".jpg");
            try {
                if (!f.exists()) {
                    if (f.createNewFile()) {
                        OutputStream os = new FileOutputStream(f);
                        os.write(data);
                        os.close();
                        listener.onComplete(f);
                    } else {
                        listener.onComplete(null);
                    }
                } else {
                    if (f.delete()) {
                        if (f.createNewFile()) {
                            OutputStream os = new FileOutputStream(f);
                            os.write(data);
                            os.close();
                            listener.onComplete(f);
                        } else {
                            listener.onComplete(null);
                        }
                    } else {
                        listener.onComplete(null);
                    }
                }
            } catch (Exception e) {
                listener.onComplete(null);
            }
        }
    }

    public Double checkReliability(Integer totalNumberOfQuestions, Double timeForAllAnswers) {
        double avg = Double.MAX_VALUE;
        double score = 0;
        if(totalNumberOfQuestions != null && totalNumberOfQuestions !=0){
            avg = timeForAllAnswers/totalNumberOfQuestions;
        }
        if(avg <= 1.0){
            score += 0.5;
        }
        else if(avg<= 2.0){
            //TODO - handle answers positions similarity
        }

        if(score == 0){
            score = -0.25;
        }

        return score;
    }
}

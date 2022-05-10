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
import com.example.appproject.model.listeners.FileListener;
import com.example.appproject.model.listeners.GetUserListener;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.listeners.GetDetailsListener;
import com.example.appproject.model.listeners.GetDetailListener;
import com.example.appproject.model.listeners.IntegerListener;
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
import com.example.appproject.model.poll.PollsListLoadingState;
import com.example.appproject.model.question.Question;
import com.example.appproject.model.listeners.BooleanListener;
import com.example.appproject.model.listeners.SaveImageListener;
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
        usersListLoadingState.setValue(UsersListLoadingState.loaded);
        pollsListLoadingState.setValue(PollsListLoadingState.loaded);
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
    
    MutableLiveData<List<User>> usersList = new MutableLiveData<>();
    MutableLiveData<UsersListLoadingState> usersListLoadingState = new MutableLiveData<>();

    public LiveData<List<User>> getUsers() {
        return usersList;
    }

    public void getUserById(String userId, GetUserListener listener) {
        executor.execute(()->{
            User user = AppLocalDb.db.userDao().loadUserById(userId);
            mainThread.post(()->listener.onComplete(user));
        });
    }

    public void refreshList() {
        usersListLoadingState.setValue(UsersListLoadingState.loading);
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("Status", Context.MODE_PRIVATE).getLong(
                MyApplication.getContext().getString(R.string.users_list_last_update_date),0);
        //Show current cache users
        executor.execute(()-> usersList.postValue(AppLocalDb.db.userDao().getAll()));
    }

    public MutableLiveData<UsersListLoadingState> getUsersListLoadingState() {
        return usersListLoadingState;
    }


    public void updateUser(String userId, Map<String,String> map, LoginListener listener) {
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


    /**
     * Data - User Details
     *
     */

    MutableLiveData<List<Question>> questionList = new MutableLiveData<>();

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
    MutableLiveData<PollsListLoadingState> pollsListLoadingState = new MutableLiveData<>();

    public LiveData<List<Poll>> getPolls() {
        return pollsList;
    }

    public MutableLiveData<PollsListLoadingState> getPollsListLoadingState() {
        return pollsListLoadingState;
    }

    public void refreshPollsList() {
        pollsListLoadingState.setValue(PollsListLoadingState.loading);
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
            pollsListLoadingState.postValue(PollsListLoadingState.loaded);
        });
    }

    private void getPollQuestionsByPollId(String pollId, GetPollQuestionsListener listener) {
        modelNode.getPollQuestionsByPollId(pollId,listener);
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

    public void updateAnswerOnLocalDb(String answerId, String chosenAnswer) {
        executor.execute(()->AppLocalDb.db.answerDao().updateAnswerByAnswerId(answerId,chosenAnswer));
    }

    public void savePollAnswersToRemoteDb(String userId, String pollId, VoidListener listener) {
        executor.execute(()->{
            List<PollWithPollQuestionsAndAnswers> pollWithPollQuestionsAndAnswers = AppLocalDb.db.pollDao().getPollWithPollQuestionsAndAnswers(pollId);
            List<PollQuestionWithAnswer> pollQuestionWithAnswer = pollWithPollQuestionsAndAnswers.get(0).pollQuestionWithAnswers;
            for(PollQuestionWithAnswer pqwa : pollQuestionWithAnswer){
                if(pqwa.answer.getUserId().equals(userId)){
                    modelNode.saveAnswerToDb(pqwa.answer,listener);
                }
            }
            executor.execute(()->{
                UserPollCrossRef userPollCrossRef = new UserPollCrossRef(MyApplication.getUserKey(),pollId);
                AppLocalDb.db.pollDao().insertAll(userPollCrossRef);
                mainThread.post(listener::onComplete);
            });
        });
    }

    public void savePollAnswersOnLocalDb(Map<String, Answer> pollMap, VoidListener listener){
        executor.execute(()->{
            for(Map.Entry<String,Answer> entry : pollMap.entrySet()){
                AppLocalDb.db.answerDao().insertAll(entry.getValue());
            }
            mainThread.post(()->listener.onComplete());
        });
    }

    public void getUserWithPolls(String userKey, GetPollsListener listener) {
        executor.execute(()->{
            List<UserWithPolls> polls = AppLocalDb.db.pollDao().getUserWithPolls(userKey);
            if(!polls.get(0).polls.isEmpty()){
                mainThread.post(()->listener.onComplete(polls.get(0).polls));
            }
            else{
                mainThread.post(()->listener.onComplete(null));
            }
        });
    }

    public void getPollQuestion(String pollQuestionId, GetPollQuestionListener listener) {
        executor.execute(()->{
           PollQuestion pollQuestion = AppLocalDb.db.pollQuestionDao().getPollQuestionById(pollQuestionId);
            mainThread.post(()->listener.onComplete(pollQuestion));
        });
    }

    public void isPollFilled(String userId,String pollId, BooleanListener listener){
        executor.execute(()->{
            List<UserWithPolls> userWithPolls = AppLocalDb.db.pollDao().getUserWithPolls(userId);
            if (!userWithPolls.isEmpty()) {
                for (Poll poll : userWithPolls.get(0).polls) {
                    if (poll.getPollId().equals(pollId)) {
                        mainThread.post(()->listener.onComplete(true));
                        return;
                    }
                }
            }
            mainThread.post(()->listener.onComplete(false));
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
                HashMap<String,String> map = new HashMap<>();
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

}

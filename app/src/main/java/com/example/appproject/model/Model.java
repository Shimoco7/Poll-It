package com.example.appproject.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.listeners.GetUserListener;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.listeners.GetDetailsListener;
import com.example.appproject.model.listeners.GetDetailListener;
import com.example.appproject.model.listeners.BitMapListener;
import com.example.appproject.model.listeners.VoidListener;
import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.listeners.GetPollQuestionListener;
import com.example.appproject.model.listeners.GetPollQuestionsListener;
import com.example.appproject.model.listeners.GetPollQuestionsWithAnswersListener;
import com.example.appproject.model.listeners.GetPollsListener;
import com.example.appproject.model.poll.Poll;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.model.poll.PollQuestionWithAnswer;
import com.example.appproject.model.poll.PollWithPollQuestionsAndAnswers;
import com.example.appproject.model.poll.PollsListLoadingState;
import com.example.appproject.model.listeners.GetPollListener;
import com.example.appproject.model.question.Question;
import com.example.appproject.model.listeners.BooleanListener;
import com.example.appproject.model.listeners.SaveImageListener;
import com.example.appproject.model.user.User;
import com.example.appproject.model.listeners.LoginListener;
import com.example.appproject.model.user.UserPollCrossRef;
import com.example.appproject.model.user.UserWithPolls;
import com.example.appproject.model.user.UsersListLoadingState;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Model {

    public static final Model instance = new Model();
    ModelFirebaseDb modelFirebaseDb = new ModelFirebaseDb();
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

    public void isSignedIn(BooleanListener booleanListener){
         modelNode.isSignedIn(booleanListener);
    }

    public void signOut(VoidListener listener) {
        modelNode.signOut(()->{
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
            listener.onComplete(user);
        });
    }

    public void refreshList() {
        usersListLoadingState.setValue(UsersListLoadingState.loading);
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("Status", Context.MODE_PRIVATE).getLong(
                MyApplication.getContext().getString(R.string.users_list_last_update_date),0);
        //Show current cache users
        executor.execute(()->{
            usersList.postValue(AppLocalDb.db.userDao().getAll());
        });
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
            listener.onComplete(detail);
        });
    }

    public void updateAnswerByDetailId(String detailId, String answer, VoidListener listener) {
        executor.execute(()->{
            AppLocalDb.db.detailDao().updateAnswerByDetailId(detailId,answer);
            listener.onComplete();
        });
    }

    public void getAllDetails(String userKey, GetDetailsListener getAllDetailsListener) {
        executor.execute(()->{
            List<Detail> list = AppLocalDb.db.detailDao().getAllDetails(userKey);
            getAllDetailsListener.onComplete(list);
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
        modelFirebaseDb.getPolls(polls->{
            executor.execute(()->{
                for(Poll poll : polls){
                    AppLocalDb.db.pollDao().insertAll(poll);
                    getPollQuestionsById(poll.getPollId(),pollQuestions->{
                        for(PollQuestion pollQuestion : pollQuestions){
                            executor.execute(()->{
                                AppLocalDb.db.pollQuestionDao().insertAll(pollQuestion);
                            });
                        }
                    });
                }
                //Update App User's List last update date
                pollsList.postValue(polls);
                pollsListLoadingState.postValue(PollsListLoadingState.loaded);
            });
        });
    }

    private void getPollQuestionsById(String pollId, GetPollQuestionsListener listener) {
        modelFirebaseDb.getPollQuestionsById(pollId,listener);
    }

    public void getPollQuestionsFromLocalDb(String pollId,GetPollQuestionsListener listener){
        executor.execute(()->{
            listener.onComplete(AppLocalDb.db.pollDao().getPollWithQuestions(pollId).get(0).pollQuestions);
        });
    }

    public void savePollAnswersOnDb(String userId,String pollId, VoidListener listener) {
        Map<String,Answer> pollMap = new HashMap<>();
        executor.execute(()->{
            List<PollWithPollQuestionsAndAnswers> pollWithPollQuestionsAndAnswers = AppLocalDb.db.pollDao().getPollWithPollQuestionsAndAnswers(pollId);
            List<PollQuestionWithAnswer> pollQuestionWithAnswer = pollWithPollQuestionsAndAnswers.get(0).pollQuestionWithAnswers;
            for(PollQuestionWithAnswer pqwa : pollQuestionWithAnswer){
                if(pqwa.answer.getUserId().equals(userId)){
                    pollMap.put(pqwa.pollQuestion.getPollQuestionId(),pqwa.answer);
                }
            }
            modelFirebaseDb.savePollAnswersOnDb(pollMap, ()->{
                executor.execute(()->{
                    UserPollCrossRef userPollCrossRef = new UserPollCrossRef(MyApplication.getUserKey(),pollId);
                    AppLocalDb.db.pollDao().insertAll(userPollCrossRef);
                    listener.onComplete();
                });
            });
        });
    }

    public void savePollAnswersOnLocalDb(Map<String, Answer> pollMap, VoidListener listener){
        executor.execute(()->{
            for(Map.Entry<String,Answer> entry : pollMap.entrySet()){
                AppLocalDb.db.answerDao().insertAll(entry.getValue());
            }
            listener.onComplete();
        });
    }

    public void getUserWithPolls(String userKey, GetPollsListener listener) {
        executor.execute(()->{
            List<UserWithPolls> polls = AppLocalDb.db.pollDao().getUserWithPolls(userKey);
            if(!polls.get(0).polls.isEmpty()){
                listener.onComplete(polls.get(0).polls);
            }
            else{
                listener.onComplete(null);
            }
        });
    }

    public void getAllAnswersByUserAndPollIds(String userId, String pollId, GetPollQuestionsWithAnswersListener listener){
        executor.execute(()->{
            HashMap<String,Answer> map = new HashMap<>();
            List<Answer> answers = AppLocalDb.db.answerDao().getAllAnswersByUserAndPollIds(userId,pollId);
            for(Answer a : answers){
                map.put(a.getPollQuestionId(),a);
            }
            listener.onComplete(map);
        });
    }

    public void getPollQuestion(String pollQuestionId, GetPollQuestionListener listener) {
        executor.execute(()->{
           PollQuestion pollQuestion = AppLocalDb.db.pollQuestionDao().getPollQuestionById(pollQuestionId);
           listener.onComplete(pollQuestion);
        });
    }


    public void getPollByPollId(String pollId, GetPollListener listener) {
        executor.execute(()->{
            Poll poll = AppLocalDb.db.pollDao().getPollByPollId(pollId);
            listener.onComplete(poll);
        });
    }


    public void isPollFilled(String userId,String pollId, BooleanListener listener){
        executor.execute(()->{
            List<UserWithPolls> userWithPolls = AppLocalDb.db.pollDao().getUserWithPolls(userId);
            if (!userWithPolls.isEmpty()) {
                for (Poll poll : userWithPolls.get(0).polls) {
                    if (poll.getPollId().equals(pollId)) {
                        listener.onComplete(true);
                        return;
                    }
                }
            }
            listener.onComplete(false);
        });
    }

    public void deletePoll(String userKey, String pollId, VoidListener listener) {

        Model.instance.getAllAnswersByUserAndPollIds(userKey,pollId,answers->{
            for(Map.Entry<String,Answer> entry : answers.entrySet()){
                entry.getValue().setDeleted(true);
            }
            modelFirebaseDb.savePollAnswersOnDb(answers,()->{
                executor.execute(()->{
                    UserPollCrossRef ref = AppLocalDb.db.pollDao().getUserPollRef(pollId,userKey);
                    executor.execute(()->{
                        AppLocalDb.db.pollDao().delete(ref);
                    });
                });
                for(Map.Entry<String,Answer> entry : answers.entrySet()){
                    executor.execute(()->{
                        AppLocalDb.db.answerDao().delete(entry.getValue());
                    });
                }
                listener.onComplete();
            });
        });
    }

    /**
     * Storage
     *
     */

    public void convertBitmapToString(Bitmap bitmap,SaveImageListener listener){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data = baos.toByteArray();
        listener.onComplete(Base64.encodeToString(data,Base64.DEFAULT));
    }

    public void convertStringToBitMap(String base64PicUrl, BitMapListener listener){
        byte[] decodedString = Base64.decode(base64PicUrl, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
        listener.onComplete(bitmap);
    }

}

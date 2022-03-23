package com.example.appproject.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.feed.GetUserByIdListener;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.detail.GetAllDetailsListener;
import com.example.appproject.model.detail.GetUserDetailByIdListener;
import com.example.appproject.model.detail.SaveDetailListener;
import com.example.appproject.model.detail.UpdateAnswerByDetailIdListener;
import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.poll.GetAnswersListener;
import com.example.appproject.model.poll.GetPollQuestionListener;
import com.example.appproject.model.poll.GetPollQuestionsListener;
import com.example.appproject.model.poll.GetPollQuestionsWithAnswersListener;
import com.example.appproject.model.poll.GetPollsListener;
import com.example.appproject.model.poll.Poll;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.model.poll.PollQuestionWithAnswer;
import com.example.appproject.model.poll.PollWithPollQuestionsAndAnswers;
import com.example.appproject.model.poll.PollsListLoadingState;
import com.example.appproject.model.poll.SavePollAnswerListener;
import com.example.appproject.model.question.GetQuestionsLocalDBListener;
import com.example.appproject.model.question.Question;
import com.example.appproject.model.user.BooleanListener;
import com.example.appproject.model.user.SaveImageListener;
import com.example.appproject.model.user.SaveUserListener;
import com.example.appproject.model.user.User;
import com.example.appproject.model.user.UserListener;
import com.example.appproject.model.user.UserPollCrossRef;
import com.example.appproject.model.user.UserWithPolls;
import com.example.appproject.model.user.UsersListLoadingState;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Model {

    public static final Model instance = new Model();
    ModelFirebaseDb modelFirebaseDb = new ModelFirebaseDb();
    ModelFirebaseAuth modelFirebaseAuth = new ModelFirebaseAuth();
    ModelFirebaseStorage modelFirebaseStorage = new ModelFirebaseStorage();
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

    public void createUser(String emailAddress, String password, UserListener userListener) {
        modelFirebaseAuth.createUser(emailAddress,password,userListener);
    }

    public void signIn(String emailAddress, String password, UserListener userListener) {
        modelFirebaseAuth.signIn(emailAddress,password, userListener);
    }

    public boolean isSignedIn(){
        return modelFirebaseAuth.isSignedIn();
    }


    public void signOut() {
        modelFirebaseAuth.signOut();
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
            MyApplication.getContext().getSharedPreferences("Status", Context.MODE_PRIVATE).edit().putLong(
                   MyApplication.getContext().getString(R.string.users_list_last_update_date),0).apply();
        });
    }

    /**
     * Data - User
     *
     */
    
    MutableLiveData<List<User>> usersList = new MutableLiveData<>();
    MutableLiveData<UsersListLoadingState> usersListLoadingState = new MutableLiveData<>();

    public void saveUserOnDb(User user, SaveUserListener saveUserListener) {
        modelFirebaseDb.SaveUserOnDb(user, saveUserListener);
    }

    public LiveData<List<User>> getUsers() {
        return usersList;
    }

    public void getUserById(String userId, GetUserByIdListener listener) {
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
        modelFirebaseDb.getUsers(list -> {
            executor.execute(()->{
                Long lud = 0L;
                for(User user : list){
                    AppLocalDb.db.userDao().insertAll(user);
                    if(lud<user.getLastUpdateDate()){
                        lud = user.getLastUpdateDate();
                    }
                }
                //Update App User's List last update date
                MyApplication.getContext().getSharedPreferences("Status", Context.MODE_PRIVATE).edit().putLong(
                        MyApplication.getContext().getString(R.string.users_list_last_update_date),lud).apply();
                usersList.postValue(AppLocalDb.db.userDao().getAll());
                List<String> userIds = new ArrayList<>();
                for(User user : list){
                    userIds.add(user.getUid());
                }
                modelFirebaseDb.getUsersWithDetails(userIds,details->{
                    for(Detail d : details){
                        executor.execute(()->{
                            AppLocalDb.db.detailDao().insertAll(d);
                        });
                    }
                });
                modelFirebaseDb.getPollQuestionsAnswers(userIds,answers->{
                    Map<String,List<String>> userPollIdsMap = new HashMap<>();
                    Set<String> pollsToDelete = new HashSet<>();
                    for(Answer a : answers){
                        if(!userPollIdsMap.containsKey(a.getUserId())){
                            userPollIdsMap.put(a.getUserId(),new ArrayList<>());
                        }
                        else{
                            Objects.requireNonNull(userPollIdsMap.get(a.getUserId())).add(a.getPollId());
                        }
                        if(a.getDeleted()){
                            pollsToDelete.add(a.getPollId());
                            executor.execute(()->{
                                AppLocalDb.db.answerDao().delete(a);
                            });
                        }
                        else{
                            executor.execute(()->{
                                AppLocalDb.db.answerDao().insertAll(a);
                            });
                        }
                    }
                    for(Map.Entry<String,List<String>> entry : userPollIdsMap.entrySet()){
                        String userId = entry.getKey();
                        List<String> pollsIds = entry.getValue();
                        for(String pollId : pollsIds){
                            UserPollCrossRef userPollCrossRef = new UserPollCrossRef(userId,pollId);
                            if(pollsToDelete.contains(pollId)){
                                executor.execute(()->{
                                    AppLocalDb.db.pollDao().delete(userPollCrossRef);
                                });
                            }
                            executor.execute(()->{
                                AppLocalDb.db.pollDao().insertAll(userPollCrossRef);
                            });
                        }

                    }
                    usersListLoadingState.postValue(UsersListLoadingState.loaded);
                });
            });
        },lastUpdateDate);
    }

    public MutableLiveData<UsersListLoadingState> getUsersListLoadingState() {
        return usersListLoadingState;
    }


    public void saveImage(Bitmap bitMap, String imageName,String folder, SaveImageListener saveImageListener) {
        modelFirebaseStorage.saveImage(bitMap,imageName,folder,saveImageListener);
    }

    public void updateUser(String userId, String key,String value, SaveUserListener saveUserListener) {
        modelFirebaseDb.updateUser(userId,key,value,saveUserListener);
    }

    public void isFinishedRegistration(BooleanListener listener) {
        modelFirebaseDb.isFinishedRegistration(listener);
    }

    public void isExist(BooleanListener listener){
        modelFirebaseDb.isExist(listener);
    }

    /**
     * Data - User Details
     *
     */
    MutableLiveData<List<Detail>> detailsList = new MutableLiveData<>();
    public MutableLiveData<String> userLocation = new MutableLiveData<>();
    public MutableLiveData<List<String>> locationsList = new MutableLiveData<>();
    MutableLiveData<List<Question>> questionList = new MutableLiveData<>();

    public void saveDetailOnLocalDb(Detail detail) {
        executor.execute(()-> AppLocalDb.db.detailDao().insertAll(detail));
    }

    public void saveDetailOnRemoteDb(Detail detail, SaveDetailListener saveDetailListener) {
        modelFirebaseDb.SaveDetailOnDb(detail, saveDetailListener);
    }


    public LiveData<List<Detail>> getDetails() {
        if (detailsList == null) { refreshDetails(); };
        return detailsList;

    }
    public void refreshDetails(){
        modelFirebaseDb.getDetails(list -> detailsList.setValue(list));
    }

    public void getUserDetailById(String userKey, String question, GetUserDetailByIdListener listener) {
        executor.execute(()->{
            Detail detail = AppLocalDb.db.detailDao().loadDetailByUserId(userKey,question);
            listener.onComplete(detail);
        });
    }

    public void updateAnswerByDetailId(String detailId, String answer, UpdateAnswerByDetailIdListener listener) {
        executor.execute(()->{
            AppLocalDb.db.detailDao().updateAnswerByDetailId(detailId,answer);
            listener.onComplete();
        });
    }

    public MutableLiveData<String> getUserLocation() {
        refreshUserLocation();
        return userLocation;
    }

    public void refreshUserLocation(){
        modelFirebaseDb.getUserLocation(location->userLocation.postValue(location));
    }


    public MutableLiveData<List<String>> getLocations() {
        refreshLocations();
        return locationsList;
    }

    public void refreshLocations(){
        modelFirebaseDb.getLocations(list->locationsList.postValue(list));
    }


    public void getAllDetails(String userKey, GetAllDetailsListener getAllDetailsListener) {
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
        ;
        return questionList;

    }

    public void refreshQuestions() {
        modelFirebaseDb.getQuestions(questions ->
                executor.execute(() -> {
                    for (Question question : questions) {
                        AppLocalDb.db.questionDao().insertAll(question);
                    }
                    questionList.postValue(questions);
                }));
    }

    public void getQuestionsFromLocalDb(GetQuestionsLocalDBListener listener) {
        executor.execute(() -> {
            listener.onComplete(AppLocalDb.db.questionDao().getAll());
        });
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

    public void savePollAnswersOnDb(String userId,String pollId, SavePollAnswerListener listener) {
        Map<String,Answer> pollMap = new HashMap<>();
        executor.execute(()->{
            List<PollWithPollQuestionsAndAnswers> pollWithPollQuestionsAndAnswers = AppLocalDb.db.pollDao().getPollWithPollQuestionsAndAnswers(pollId);
            List<PollQuestionWithAnswer> pollQuestionWithAnswer = pollWithPollQuestionsAndAnswers.get(0).pollQuestionWithAnswers;
            for(PollQuestionWithAnswer pqwa : pollQuestionWithAnswer){
                if(pqwa.answer.getUserId().equals(userId)){
                    pollMap.put(pqwa.pollQuestion.getPollQuestionId(),pqwa.answer);
                }
            }
            modelFirebaseDb.SavePollAnswersOnDb(pollMap, ()->{
                executor.execute(()->{
                    UserPollCrossRef userPollCrossRef = new UserPollCrossRef(MyApplication.getUserKey(),pollId);
                    AppLocalDb.db.pollDao().insertAll(userPollCrossRef);
                    listener.onComplete();
                });
            });
        });
    }

    public void savePollAnswersOnLocalDb(Map<String, Answer> pollMap, SavePollAnswerListener listener){
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

    public void isPollFilled(String userId,String pollId, BooleanListener listener){
        executor.execute(()->{
            List<UserWithPolls> userWithPolls = AppLocalDb.db.pollDao().getUserWithPolls(userId);
            for(Poll poll : userWithPolls.get(0).polls){
                if(poll.getPollId().equals(pollId)){
                    listener.onComplete(true);
                }
            }
            listener.onComplete(false);
        });
    }
}

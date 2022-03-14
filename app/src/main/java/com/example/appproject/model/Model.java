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
import com.example.appproject.model.detail.GetUserDetailByIdListener;
import com.example.appproject.model.detail.SaveDetailListener;
import com.example.appproject.model.question.Question;
import com.example.appproject.model.user.SaveImageListener;
import com.example.appproject.model.user.SaveUserListener;
import com.example.appproject.model.user.User;
import com.example.appproject.model.user.UserListener;
import com.example.appproject.model.user.UsersListLoadingState;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.List;
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
        final String nameRegex = "^[a-zA-Z\\s]+";
        return name.matches(nameRegex);
    }

    public boolean validateAddress(String address){
        final String addressRegex = "^[a-zA-Z\\s]+";
        final String addressRegexNumeric = "-?\\d+(\\.\\d+)?";
        String[] addressList = address.split(",");
        if(addressList.length!=4) {
            return false;
        }
        for(int i=0; i<3; i++){
            if(!addressList[i].matches(addressRegex)){
                return false;
            }
        }

        return addressList[3].matches(addressRegexNumeric);

    }

    public void clearCaches() {
        executor.execute(()->{
            AppLocalDb.db.clearAllTables();
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
        modelFirebaseDb.SaveUserOnDb(user, saveUserListener::onComplete);
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
                usersListLoadingState.postValue(UsersListLoadingState.loaded);
            });
        },lastUpdateDate);
    }

    public MutableLiveData<UsersListLoadingState> getUsersListLoadingState() {
        return usersListLoadingState;
    }


    public void saveImage(Bitmap bitMap, String imageName, SaveImageListener saveImageListener) {
        modelFirebaseStorage.saveImage(bitMap,imageName,saveImageListener);
    }

    public void updateUser(String userId, String key,String value, SaveUserListener saveUserListener) {
        modelFirebaseDb.setUserProfilePicUrl(userId,key,value,saveUserListener);
    }

    /**
     * Data - User Details
     *
     */
    MutableLiveData<List<Detail>> detailsList = new MutableLiveData<>();
    MutableLiveData<List<Question>> questionList = new MutableLiveData<>();

    public void saveDetailOnDb(Detail detail, SaveDetailListener saveDetailListener) {
        modelFirebaseDb.SaveDetailOnDb(detail, saveDetailListener);
        executor.execute(()->{
            AppLocalDb.db.detailDao().insertAll(detail);
        });
    }
    public LiveData<List<Detail>> getDetails() {
        if (detailsList == null) { refreshDetails(); };
        return detailsList;

    }
    public void refreshDetails(){
        modelFirebaseDb.getDetails(list -> detailsList.setValue(list));
    }

    public LiveData<List<Question>> getQuestions() {
        if (questionList == null) { refreshQuestions(); };
        return questionList;

    }
    public void refreshQuestions(){
        modelFirebaseDb.getQuestions(list -> questionList.setValue(list));
    }

    public void getUserDetailById(String userKey, String question, GetUserDetailByIdListener listener) {
        executor.execute(()->{
            Detail detail = AppLocalDb.db.detailDao().loadDetailByUserId(userKey,question);
            listener.onComplete(detail);
        });
    }
}

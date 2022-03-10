package com.example.appproject.model;


import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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


    /**
     * Data
     *
     */
    
    MutableLiveData<List<User>> usersList = new MutableLiveData<>();
    MutableLiveData<UsersListLoadingState> usersListLoadingState = new MutableLiveData<>();

    public void saveUserOnDb(User user, SaveUserListener saveUserListener) {
        modelFirebaseDb.SaveUserOnDb(user, saveUserListener::onComplete);
    }

    public LiveData<List<User>> getUsers() {
        if(usersList.getValue() == null){
            refreshList();
        }
        return usersList;
    }

    public void refreshList() {
        usersListLoadingState.setValue(UsersListLoadingState.loading);
        executor.execute(()->{
            usersList.postValue(AppLocalDb.db.userDao().getAll().getValue());
        });
        modelFirebaseDb.getUsers(list -> {
            executor.execute(()->{
                for(User user : list){
                    AppLocalDb.db.userDao().insertAll(user);
                }
                usersList.postValue(list);
                usersListLoadingState.postValue(UsersListLoadingState.loaded);
            });
        });
    }

    public MutableLiveData<UsersListLoadingState> getUsersListLoadingState() {
        return usersListLoadingState;
    }
}

package com.example.appproject.model;


import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Model {

    public static final Model instance = new Model();
    ModelFirebaseDb modelFirebaseDb = new ModelFirebaseDb();
    ModelFirebaseAuth modelFirebaseAuth = new ModelFirebaseAuth();
    Executor executor = Executors.newSingleThreadExecutor();
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    private Model(){}

    public Executor getExecutor() {
        return executor;
    }

    public Handler getMainThread() {
        return mainThread;
    }

    /**
    * Authentication
     * @return
     */

    public void signIn(String emailAddress, String password,SignInListener signInListener) {
        modelFirebaseAuth.signIn(emailAddress,password,signInListener);
    }

    public boolean isSignedIn(){
        return modelFirebaseAuth.isSignedIn();
    }


    public void createUser(String emailAddress, String password) {
        modelFirebaseAuth.createUser(emailAddress,password);
    }

    public boolean validateEmailAddress(String emailAddress) {
        return EmailValidator.getInstance().isValid(emailAddress);
    }

    public boolean validatePassword(String password) {
        final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,20}$";
        return password.matches(regex);
    }

    public void signOut() {
        modelFirebaseAuth.signOut();
    }
}

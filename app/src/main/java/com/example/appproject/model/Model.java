package com.example.appproject.model;


import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
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
    */

    public boolean isSignedIn(){
        return modelFirebaseAuth.isSignedIn();
    }


}

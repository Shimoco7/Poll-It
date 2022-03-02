package com.example.appproject.model;


import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Model {

    public static final Model instance = new Model();
    Executor executor = Executors.newSingleThreadExecutor();
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    private Model(){}
}

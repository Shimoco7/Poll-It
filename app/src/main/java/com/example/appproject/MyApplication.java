package com.example.appproject;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

    public static void setUserKey(String uid){
        context.getSharedPreferences("Status",Context.MODE_PRIVATE)
                .edit()
                .putString("firebasekey",uid)
                .apply();
    }

    public static String getUserKey(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString("firebasekey","");
    }
}

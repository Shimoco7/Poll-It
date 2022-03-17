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
                .putString(context.getString(R.string.firebasekey),uid)
                .apply();
    }

    public static String getUserKey(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString(context.getString(R.string.firebasekey),"");
    }

    public static void setUserEmail(String email){
        context.getSharedPreferences("Status",Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.user_email),email)
                .apply();
    }

    public static String getUserEmail(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString(context.getString(R.string.user_email),"");
    }

    public static void setUserName(String name){
        context.getSharedPreferences("Status",Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.user_name),name)
                .apply();
    }

    public static String getUserName(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString(context.getString(R.string.user_name),"");
    }

    public static void setUserAddress(String address){
        context.getSharedPreferences("Status",Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.user_address),address)
                .apply();
    }

    public static String getUserAddress(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString(context.getString(R.string.user_address),"");
    }

}

package com.example.appproject;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

public class MyApplication extends Application {
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        AppEventsLogger.activateApp(this);
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


    public static void setUserProfilePicUrl(String url){
        context.getSharedPreferences("Status",Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.profile_pic_url),url)
                .apply();
    }

    public static String getUserProfilePicUrl(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString(context.getString(R.string.profile_pic_url),"");
    }

    public static void setAccessToken(String accessToken) {
        context.getSharedPreferences("Status",Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.access_token),accessToken)
                .apply();
    }

    public static String getAccessToken(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString(context.getString(R.string.access_token),null);
    }

    public static void setRefreshToken(String refreshToken) {
        context.getSharedPreferences("Status",Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.refresh_token),refreshToken)
                .apply();
    }

    public static String getRefreshToken(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString(context.getString(R.string.refresh_token),null);
    }

    public static void setGender(String gender) {
        context.getSharedPreferences("Status",Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.gender),gender)
                .apply();
    }

    public static String getGender(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString(context.getString(R.string.gender),"");
    }

    public static void setFacebookId(String facebookId) {
        context.getSharedPreferences("Status",Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.current_account_facebook_id),facebookId)
                .apply();
    }

    public static String getFacebookId(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString(context.getString(R.string.current_account_facebook_id),"");
    }

    public static void setUserCoins(String userCoins) {
        context.getSharedPreferences("Status",Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.user_coins),userCoins)
                .apply();
    }

    public static String getUserCoins(){
        return context.getSharedPreferences("Status", Context.MODE_PRIVATE).getString(context.getString(R.string.user_coins),"");
    }

}

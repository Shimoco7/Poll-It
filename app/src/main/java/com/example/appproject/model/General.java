package com.example.appproject.model;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class General {

    public static void enableDisableClickView(View view, boolean enabled) {
        view.setClickable(enabled);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableClickView(group.getChildAt(idx), enabled);
            }
        }
    }

    public static void progressBarOn(Activity activity, ViewGroup container, ProgressBar progressBar, Boolean showBackButton) {
        Model.instance.getMainThread().post(()->{
            progressBar.setVisibility(View.VISIBLE);
            General.enableDisableClickView(container, false);
            if( ((AppCompatActivity) activity).getSupportActionBar() != null){
                ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
            }
        });
    }

    public static void progressBarOff(Activity activity, ViewGroup container, ProgressBar progressBar, Boolean showBackButton) {
        Model.instance.getMainThread().post(()->{
            progressBar.setVisibility(View.GONE);
            General.enableDisableClickView(container, true);
            if(((AppCompatActivity) activity).getSupportActionBar() != null){
                ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
            }
        });
    }

    public static void loadImage(String url, ImageView imageView, int placeholder){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + MyApplication.getAccessToken())
                    .build();
            return chain.proceed(newRequest);
        }).build();
        Picasso picasso = new Picasso.Builder(MyApplication.getContext()).downloader(new OkHttp3Downloader(client)).build();
        picasso.load(url)
                .placeholder(placeholder)
                .into(imageView);
    }

}

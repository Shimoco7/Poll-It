package com.example.appproject.model;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

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

    public static void progressBarOn(Activity activity, ViewGroup container, ProgressBar progressBar) {
        Model.instance.getMainThread().post(()->{
            progressBar.setVisibility(View.VISIBLE);
            General.enableDisableClickView(container, false);
            ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        });
    }

    public static void progressBarOff(Activity activity, ViewGroup container, ProgressBar progressBar) {
        Model.instance.getMainThread().post(()->{
            progressBar.setVisibility(View.GONE);
            General.enableDisableClickView(container, true);
            ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        });
    }


}

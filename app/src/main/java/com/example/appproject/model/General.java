package com.example.appproject.model;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class General {
    public static void showToast(Activity activity, ArrayList<String> errors){
        StringBuilder message = new StringBuilder();
        for (String error : errors) {
            message.append(error);
            message.append("\n");
        }
        Toast.makeText(activity, message.toString().trim(),
                Toast.LENGTH_LONG).show();
    }

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
        progressBar.setVisibility(View.VISIBLE);
        General.enableDisableClickView(container, false);
        ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public static void progressBarOff(Activity activity, ViewGroup container, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        General.enableDisableClickView(container, true);
        ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

package com.example.appproject.model;

import android.app.Activity;
import android.widget.Toast;

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
}

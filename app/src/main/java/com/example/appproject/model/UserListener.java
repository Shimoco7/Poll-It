package com.example.appproject.model;

import com.google.firebase.auth.FirebaseUser;

public interface UserListener {
    void onComplete(FirebaseUser user, String message);
}

package com.example.appproject.model;

import com.google.firebase.auth.FirebaseUser;

public interface SignInListener {
    void onComplete(FirebaseUser user);
}

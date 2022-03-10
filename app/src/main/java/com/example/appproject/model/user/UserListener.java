package com.example.appproject.model.user;

import com.google.firebase.auth.FirebaseUser;

public interface UserListener {
    void onComplete(FirebaseUser user, String message);
}

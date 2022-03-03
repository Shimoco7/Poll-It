package com.example.appproject.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ModelFirebaseAuth {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public boolean isSignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }
}

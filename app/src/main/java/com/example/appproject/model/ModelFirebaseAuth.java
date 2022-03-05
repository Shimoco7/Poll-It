package com.example.appproject.model;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ModelFirebaseAuth {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public boolean isSignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public void createUser(String emailAddress, String password,UserListener userListener) {
        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        userListener.onComplete(user);
                    } else {
                        Log.d("TAG", "createUserWithEmail:failure", task.getException());
                        userListener.onComplete(null);
                    }
                });
    }

    public void signIn(String emailAddress, String password, UserListener userListener){
        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        userListener.onComplete(user);
                    } else {
                        Log.d("TAG", "signInWithEmail:failure", task.getException());
                        userListener.onComplete(null);
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }
}

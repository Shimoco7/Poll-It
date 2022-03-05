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

    public void createUser(String emailAddress, String password) {
        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("TAG", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                });
    }

    public void signIn(String emailAddress, String password, SignInListener signInListener){
        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        signInListener.onComplete(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("TAG", "signInWithEmail:failure", task.getException());
                        signInListener.onComplete(null);
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }
}

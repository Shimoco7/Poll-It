package com.example.appproject.model;

import android.content.Context;
import android.util.Log;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.user.UserListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class ModelFirebaseAuth {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Context appContext = MyApplication.getContext();

    public boolean isSignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public void createUser(String emailAddress, String password, UserListener userListener) {
        Model.instance.getExecutor().execute(()->{
            mAuth.createUserWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Model.instance.getMainThread().post(()->{
                                userListener.onComplete(user, appContext.getString(R.string.success));
                            });
                        } else {
                            Log.d("TAG", "createUserWithEmail:failure", task.getException());
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Model.instance.getMainThread().post(()->{
                                    userListener.onComplete(null, appContext.getString(R.string.success));
                                });
                            }
                            else{
                                Model.instance.getMainThread().post(()->{
                                    userListener.onComplete(null, appContext.getString(R.string.success));
                                });
                            }
                        }
                    });
        });
    }

    public void signIn(String emailAddress, String password, UserListener userListener){
        Model.instance.getExecutor().execute(()->{
            mAuth.signInWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Model.instance.getMainThread().post(()->{
                                userListener.onComplete(user, appContext.getString(R.string.success));
                            });
                        } else {
                            Log.d("TAG", "signInWithEmail:failure", task.getException());
                            Model.instance.getMainThread().post(()->{
                                userListener.onComplete(null, null); //TODO
                            });
                        }
                    });
        });
    }

    public void signOut() {
        mAuth.signOut();
    }
}

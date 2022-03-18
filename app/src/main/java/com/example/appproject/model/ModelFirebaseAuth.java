package com.example.appproject.model;

import android.content.Context;
import android.util.Log;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.model.user.User;
import com.example.appproject.model.user.UserListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class ModelFirebaseAuth {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    Context appContext = MyApplication.getContext();

    public ModelFirebaseAuth() {
        setAuthStateListener();
    }

    private void setAuthStateListener() {
        authStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                String uid = firebaseUser.getUid();
                MyApplication.setUserKey(uid);
            }
            else{
                Model.instance.clearCaches();
                MyApplication.setUserKey("");
            }
        };
        mAuth.addAuthStateListener(authStateListener);
    }

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
                            assert user != null;
                            MyApplication.setUserEmail(user.getEmail());
                            Model.instance.getMainThread().post(()->{
                                userListener.onComplete(user, appContext.getString(R.string.success));
                            });
                        } else {
                            Log.d("TAG", "createUserWithEmail:failure", task.getException());
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Model.instance.getMainThread().post(()->{
                                    userListener.onComplete(null, appContext.getString(R.string.user_already_exists));
                                });
                            }
                            else{
                                Model.instance.getMainThread().post(()->{
                                    userListener.onComplete(null, appContext.getString(R.string.registration_failed));
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
                            assert user != null;
                            Model.instance.modelFirebaseDb.getUserById(user.getUid(), list->{
                                User u = list.get(0);
                                MyApplication.setUserEmail(u.getEmail());
                                MyApplication.setUserName(u.getName());
                                MyApplication.setUserAddress(u.getAddress());
                                MyApplication.setUserProfilePicUrl(u.getProfilePicUrl());
                            });
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
        appContext.getSharedPreferences("Status",Context.MODE_PRIVATE).edit().clear().apply();
        mAuth.signOut();
    }
}

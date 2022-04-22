package com.example.appproject.model.listeners;


import com.example.appproject.model.user.User;

public interface LoginListener {
    void onComplete(User user, String message);
}

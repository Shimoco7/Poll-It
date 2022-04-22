package com.example.appproject.model.listeners;

import com.example.appproject.model.user.User;

import java.util.List;

public interface GetUsersListener {
    void onComplete(List<User> list);
}

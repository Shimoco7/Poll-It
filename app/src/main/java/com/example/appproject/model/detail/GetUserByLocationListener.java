package com.example.appproject.model.detail;

import com.example.appproject.model.user.User;

import java.util.List;

public interface GetUserByLocationListener {
    void onComplete(List<User> list);
}

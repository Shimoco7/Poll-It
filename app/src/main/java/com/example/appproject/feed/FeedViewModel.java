package com.example.appproject.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.appproject.model.Model;
import com.example.appproject.model.user.User;

import java.util.List;

public class FeedViewModel extends ViewModel {
    LiveData<List<User>> users;

    public FeedViewModel() {
        users = Model.instance.getUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }
}

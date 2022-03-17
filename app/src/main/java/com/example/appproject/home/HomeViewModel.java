package com.example.appproject.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.appproject.model.Model;
import com.example.appproject.model.poll.Poll;

import java.util.List;

public class HomeViewModel extends ViewModel {
    LiveData<List<Poll>> polls;

    public HomeViewModel() {
        polls = Model.instance.getPolls();
    }

    public LiveData<List<Poll>> getPolls() {
        return polls;
    }
}

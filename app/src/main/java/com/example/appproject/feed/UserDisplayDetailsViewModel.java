package com.example.appproject.feed;

import androidx.lifecycle.ViewModel;

import com.example.appproject.model.poll.Poll;

import java.util.List;

public class UserDisplayDetailsViewModel extends ViewModel {
    List<Poll> userFilledPolls;

    public UserDisplayDetailsViewModel() { }

    public void setUserFilledPolls(List<Poll> userFilledPolls) {
        this.userFilledPolls = userFilledPolls;
    }

    public List<Poll> getUserFilledPolls() {
        return userFilledPolls;
    }
}

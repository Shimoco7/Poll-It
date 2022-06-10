package com.example.appproject.displaydetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.appproject.model.Model;
import com.example.appproject.model.poll.Poll;

import java.util.List;

public class UserDisplayDetailsViewModel extends ViewModel {
    List<Poll> userFilledPolls;
    LiveData<Boolean> isPassChanged;
    LiveData<Boolean> isDetailsChanged;
    String userId;

    public UserDisplayDetailsViewModel() {
        isPassChanged = Model.instance.getIsPassChanged();
        isDetailsChanged = Model.instance.getIsDetailsChanged();
    }

    public void setUserFilledPolls(List<Poll> userFilledPolls) {
        this.userFilledPolls = userFilledPolls;
    }

    public List<Poll> getUserFilledPolls() {
        return userFilledPolls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LiveData<Boolean> getIsPassChanged() {
        return isPassChanged;
    }

    public LiveData<Boolean> getIsDetailsChanged() {
        return isDetailsChanged;
    }
}

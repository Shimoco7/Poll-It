package com.example.appproject.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.appproject.model.Model;
import com.example.appproject.model.reward.Reward;

import java.util.List;

public class RewardCenterViewModel extends ViewModel {
    LiveData<List<Reward>> rewards;

    public RewardCenterViewModel() {
        rewards = Model.instance.getRewards();
    }

    public LiveData<List<Reward>> getRewards() {
        return rewards;
    }
}

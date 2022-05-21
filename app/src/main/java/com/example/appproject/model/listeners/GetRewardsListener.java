package com.example.appproject.model.listeners;

import com.example.appproject.model.reward.Reward;

import java.util.List;

public interface GetRewardsListener {
    void onComplete(List<Reward> rewards);
}

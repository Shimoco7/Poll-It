package com.example.appproject.model.listeners;

import com.example.appproject.model.poll.Poll;

import java.util.List;

public interface GetPollsListener {
    void onComplete(List<Poll> list);
}

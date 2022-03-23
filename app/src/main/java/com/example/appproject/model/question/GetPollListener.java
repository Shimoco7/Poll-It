package com.example.appproject.model.question;

import com.example.appproject.model.poll.Poll;

import java.util.List;

public interface GetPollListener {
    void onComplete(Poll poll);
}

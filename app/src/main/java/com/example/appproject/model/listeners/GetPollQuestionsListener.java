package com.example.appproject.model.listeners;

import com.example.appproject.model.poll.PollQuestion;

import java.util.List;

public interface GetPollQuestionsListener {
    void onComplete(List<PollQuestion> list);
}

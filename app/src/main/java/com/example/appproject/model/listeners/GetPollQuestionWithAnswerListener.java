package com.example.appproject.model.listeners;

import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.poll.PollQuestionWithAnswer;

import java.util.Map;

public interface GetPollQuestionWithAnswerListener {
    void onComplete(PollQuestionWithAnswer pollQuestionWithAnswer);
}

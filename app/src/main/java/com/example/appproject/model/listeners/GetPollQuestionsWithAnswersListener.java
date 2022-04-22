package com.example.appproject.model.listeners;

import com.example.appproject.model.poll.Answer;

import java.util.Map;

public interface GetPollQuestionsWithAnswersListener {
    void onComplete(Map<String, Answer> map);
}

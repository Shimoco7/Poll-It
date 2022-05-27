package com.example.appproject.model.listeners;

import com.example.appproject.model.poll.PollQuestion;

public interface GetPollQuestionWithBooleanListener {
    void onComplete(PollQuestion pollQuestion, Boolean isSingleQuestionAndAnswered);
}

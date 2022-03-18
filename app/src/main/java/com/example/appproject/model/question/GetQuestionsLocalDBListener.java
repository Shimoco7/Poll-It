package com.example.appproject.model.question;

import com.example.appproject.model.poll.PollQuestion;

import java.util.List;

public interface GetQuestionsLocalDBListener {
    void onComplete(List<Question> list);
}

package com.example.appproject.model.listeners;

import com.example.appproject.model.poll.Answer;

import java.util.List;

public interface GetAnswersListener {
    void onComplete(List<Answer> list);
}

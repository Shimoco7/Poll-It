package com.example.appproject.model.listeners;

import com.example.appproject.model.question.Question;

import java.util.List;

public interface GetQuestionsListener {
    void onComplete(List<Question> list);
}

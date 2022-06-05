package com.example.appproject.model.listeners;

import java.util.List;

public interface AnswersListener {
    void onComplete(Double timeForAllAnswers, List<Integer> answersIndices);
}

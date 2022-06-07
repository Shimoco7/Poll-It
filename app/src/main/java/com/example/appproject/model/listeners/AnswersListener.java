package com.example.appproject.model.listeners;

import android.util.Pair;

import java.util.List;

public interface AnswersListener {
    void onComplete(Double timeForAllAnswers, List<Pair<Integer,Integer>> answersIndices);
}

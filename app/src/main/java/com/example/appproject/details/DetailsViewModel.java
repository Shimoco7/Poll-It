package com.example.appproject.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.appproject.model.Model;
import com.example.appproject.model.question.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsViewModel extends ViewModel {
    Map<String, String> answersMap;
    LiveData<List<Question>> questions;

    public DetailsViewModel() {
        questions = Model.instance.getQuestions();
        answersMap = new HashMap<>();

    }

    public LiveData<List<Question>> getQuestions() {
        return questions;
    }
    public Map<String, String> getAnswersMap() {
        return answersMap;
    }
}

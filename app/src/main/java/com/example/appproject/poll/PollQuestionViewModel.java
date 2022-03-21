package com.example.appproject.poll;

import androidx.lifecycle.ViewModel;

import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.poll.PollQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollQuestionViewModel extends ViewModel {
    List<PollQuestion> pollQuestions;
    Map<String, Answer> pollMap;
    String imagePollQuestionId;
    int index=0;


    public PollQuestionViewModel() {
        pollQuestions = new ArrayList<>();
        pollMap = new HashMap<>();
    }

    public List<PollQuestion> getPollQuestions() {
        return pollQuestions;
    }

    public void setPollQuestions(List<PollQuestion> pollQuestions) {
        this.pollQuestions = pollQuestions;
    }

    public Map<String, Answer> getPollMap() {
        return pollMap;
    }

    public void setPollMap(Map<String, Answer> pollMap) {
        this.pollMap = pollMap;
    }

    public String getImagePollQuestionId() {
        return imagePollQuestionId;
    }

    public void setImagePollQuestionId(String imagePollQuestionId) {
        this.imagePollQuestionId = imagePollQuestionId;
    }
}

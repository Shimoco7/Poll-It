package com.example.appproject.poll;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appproject.model.poll.Answer;
import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.model.poll.PollQuestionWithAnswer;

import java.util.HashMap;
import java.util.Map;

public class PollQuestionMultiChoiceViewModel extends ViewModel {

    MutableLiveData<PollQuestionWithAnswer> pollQuestionWithAnswer;
    Integer totalPollNumberOfQuestions;

    public PollQuestionMultiChoiceViewModel() {
        pollQuestionWithAnswer = new MutableLiveData<>();
    }

    public Integer getTotalPollNumberOfQuestions() {
        return totalPollNumberOfQuestions;
    }

    public void setTotalPollNumberOfQuestions(Integer totalPollNumberOfQuestions) {
        this.totalPollNumberOfQuestions = totalPollNumberOfQuestions;
    }

    public MutableLiveData<PollQuestionWithAnswer> getPollQuestionWithAnswer() {
        return pollQuestionWithAnswer;
    }

    public void setPollQuestionWithAnswer(PollQuestionWithAnswer pollQuestionWithAnswer) {
        this.pollQuestionWithAnswer.postValue(pollQuestionWithAnswer);
    }
}

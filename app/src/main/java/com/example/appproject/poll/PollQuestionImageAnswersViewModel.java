package com.example.appproject.poll;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appproject.model.poll.PollQuestion;
import com.example.appproject.model.poll.PollQuestionWithAnswer;

public class PollQuestionImageAnswersViewModel extends ViewModel {

    private MutableLiveData<PollQuestionWithAnswer> pollQuestionWithAnswer;
    private Integer totalPollNumberOfQuestions;
    private PollQuestion nextPollQuestion;
    private Boolean isAnswered;

    public PollQuestionImageAnswersViewModel() {
        pollQuestionWithAnswer = new MutableLiveData<>();
        isAnswered = false;
    }

    public MutableLiveData<PollQuestionWithAnswer> getPollQuestionWithAnswer() {
        return pollQuestionWithAnswer;
    }

    public void setPollQuestionWithAnswer(PollQuestionWithAnswer pollQuestionWithAnswer) {
        this.pollQuestionWithAnswer.postValue(pollQuestionWithAnswer);
    }

    public Integer getTotalPollNumberOfQuestions() {
        return totalPollNumberOfQuestions;
    }

    public void setTotalPollNumberOfQuestions(Integer totalPollNumberOfQuestions) {
        this.totalPollNumberOfQuestions = totalPollNumberOfQuestions;
    }

    public PollQuestion getNextPollQuestion() {
        return nextPollQuestion;
    }

    public void setNextPollQuestion(PollQuestion nextPollQuestion) {
        this.nextPollQuestion = nextPollQuestion;
    }

    public Boolean getAnswered() {
        return isAnswered;
    }

    public void setAnswered(Boolean answered) {
        isAnswered = answered;
    }
}

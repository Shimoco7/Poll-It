package com.example.appproject.poll;

import androidx.lifecycle.ViewModel;

import com.example.appproject.model.poll.PollQuestion;

public class PollImageViewModel extends ViewModel {
    PollQuestion pollQuestion;

    public PollQuestion getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(PollQuestion pollQuestion) {
        this.pollQuestion = pollQuestion;
    }
}

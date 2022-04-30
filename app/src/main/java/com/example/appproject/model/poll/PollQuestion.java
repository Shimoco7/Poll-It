package com.example.appproject.model.poll;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.appproject.poll.PollQuestionType;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class PollQuestion {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    public String pollQuestionId;
    @NonNull
    public String pollQuestion;
    @NonNull
    public String pollId;
    public String pollQuestionImage;
    public PollQuestionType pollQuestionType;
    public List<String> choices;
    public Integer questionNumber;

    public PollQuestion() { }

    @NonNull
    public String getPollQuestionId() {
        return pollQuestionId;
    }

    public void setPollQuestionId(@NonNull String pollQuestionId) {
        this.pollQuestionId = pollQuestionId;
    }

    @NonNull
    public String getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(@NonNull String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    @NonNull
    public String getPollId() {
        return pollId;
    }

    public void setPollId(@NonNull String pollId) {
        this.pollId = pollId;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getPollQuestionImage() {
        return pollQuestionImage;
    }

    public void setPollQuestionImage(String pollQuestionImage) {
        this.pollQuestionImage = pollQuestionImage;
    }

    public PollQuestionType getPollQuestionType() {
        return pollQuestionType;
    }

    public void setPollQuestionType(PollQuestionType pollQuestionType) {
        this.pollQuestionType = pollQuestionType;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }
}

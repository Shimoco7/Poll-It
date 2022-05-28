package com.example.appproject.model.question;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    public String questionId;
    @SerializedName("detailQuestion")
    public String question;
    @SerializedName("choices")
    public List<String> multiChoice;

    public Question() { }

    @NonNull
    public String getQuestionId() {
        return questionId;
    }
    public void setQuestionId(@NonNull String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getMultiChoice() {
        return multiChoice;
    }
    public void setMultiChoice(ArrayList<String> multiChoice) {
        this.multiChoice = multiChoice;
    }

}

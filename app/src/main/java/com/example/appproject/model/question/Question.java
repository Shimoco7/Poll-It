package com.example.appproject.model.question;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
public class Question {

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    public String questionId;
    public String question;
    @SerializedName("multi_choice")
    public List<String> multiChoice;



    Stopwatch stopwatch;

    public Question() { }

    @Ignore
    public Question(@NonNull String question) {
        this.questionId = UUID.randomUUID().toString();
        this.question = question;
        multiChoice = new ArrayList<>();
    }

    @Ignore
    public Question(@NonNull String questionId, String question, List<String> multiChoice) {
        this.questionId = questionId;
        this.question = question;
        this.multiChoice =multiChoice;
    }

    /**
     * Factory
     *
     */

    public static Question create(Map<String, Object> data) {
        String questionId = (String)data.get("question_id");
        String question = (String)data.get("question");
        List<String> multiChoice = (List<String>)data.get("multi_choice");

        return new Question(questionId,question,multiChoice);
    }

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

    public Stopwatch getStopwatch() {
        return stopwatch;

    }

    public void startWatch() {

        this.stopwatch.start();
    }
    public void Stopwatch() {
        this.stopwatch.stop();
    }


}

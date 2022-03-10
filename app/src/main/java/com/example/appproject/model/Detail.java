package com.example.appproject.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Map;

@Entity
public class Detail {

    @PrimaryKey
    @NonNull
    String uid;
    String question;
    ArrayList<String> answers;

    public Detail() { }

    @Ignore
    public Detail(@NonNull String uid) {
        this.uid = uid;
        setQuestion(uid);
        answers = new ArrayList<>();
        answers.add("answer1");
        answers.add("answer2");
        answers.add("answer3");
        answers.add("answer4");
    }

    /**
     * Factory
     *
     */
    public static Detail create(Map<String, Object> data) {
        String uid = (String) data.get("uid");
        String question = (String) data.get("personal_question");
        ArrayList<String> answers = (ArrayList<String>) data.get("personal_answers");

        Detail detail = new Detail(uid);
        detail.setQuestion(question);
        detail.setAnswers(answers);
        return detail;
    }

    @NonNull
    public String getUid() {
        return uid;
    }
    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> question) {
        this.answers = question;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}

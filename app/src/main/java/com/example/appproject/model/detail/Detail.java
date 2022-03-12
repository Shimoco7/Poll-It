package com.example.appproject.model.detail;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.appproject.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
public class Detail {

    @PrimaryKey
    @NonNull
    public String detailId;
    public String userUid;
    public String question;
    public String finalAnswer;
    public List<String> answers;

    public Detail() { }

    @Ignore
    public Detail(@NonNull String question) {
        this.question = question;
        this.userUid = MyApplication.getUserKey();
        this.detailId = UUID.randomUUID().toString();
        answers = new ArrayList<>();
    }

    @Ignore
    public Detail(@NonNull String question, String finalAnswer) {
        this.userUid = MyApplication.getUserKey();
        this.detailId = UUID.randomUUID().toString();
        this.question = question;
        this.finalAnswer = finalAnswer;
        this.setAnswers(new ArrayList<>());
    }

    @Ignore
    public Detail(@NonNull String question, List<String> multiChoice) {
        this.userUid = MyApplication.getUserKey();
        this.detailId = UUID.randomUUID().toString();
        this.question = question;
        this.finalAnswer = "";
        this.answers =multiChoice;
    }

    /**
     * Factory
     *
     */
    public static Detail create(Map<String, Object> data) {

        String question = (String)data.get("question");
        String answer = (String)data.get("answer");

        return new Detail(question,answer);
    }

    public static Detail createQuestion(Map<String, Object> data) {

        String question = (String)data.get("question");
        List<String> multiChoice = (List<String>)data.get("multi_choice");

        return new Detail(question,multiChoice);
    }

    @NonNull
    public String getUserUid() {
        return userUid;
    }
    public void setUserUid(@NonNull String userUid) {
        this.userUid = userUid;
    }

    @NonNull
    public String getDetailId() {
        return detailId;
    }
    public void setDetailId(@NonNull String detailid) {
        this.detailId = detailId;
    }

    public String getFinalAnswer() {
        return finalAnswer;
    }
    public void setFinalAnswer(String finalAnswer) {
        this.finalAnswer = finalAnswer;
    }

    public List<String> getAnswers() {
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

    public Map<String,Object> toJson(){
        Map<String,Object> json = new HashMap<>();
        json.put("detail_id", detailId);
        json.put("uid", userUid);
        json.put("question",question);
        json.put("answer",finalAnswer);
        return json;
    }

}

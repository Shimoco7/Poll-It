package com.example.appproject.model.detail;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Detail {

    @PrimaryKey
    @NonNull
    String uid;
    String question;
    String finalAnswer;
    ArrayList<String> answers;

    public Detail() { }

    @Ignore
    public Detail(@NonNull String uid) {
        this.uid = uid;
        setQuestion(uid);
        answers = new ArrayList<>();
        finalAnswer = "";
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

    public String getFinalAnswer() {
        return finalAnswer;
    }
    public void setFinalAnswer(String finalAnswer) {
        this.finalAnswer = finalAnswer;
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

    public Map<String,Object> toJson(){
        Map<String,Object> json = new HashMap<>();
        json.put("uid",uid);
        json.put("personal_question",question);
        json.put("optional_questions", new JSONArray(answers));
        json.put("final_answer",finalAnswer);
        return json;
    }

}

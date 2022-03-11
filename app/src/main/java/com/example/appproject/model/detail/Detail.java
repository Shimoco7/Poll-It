package com.example.appproject.model.detail;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.appproject.MyApplication;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Detail {

    @PrimaryKey
    @NonNull
    public String detailId;
    public String uid;
    public String question;
    public String finalAnswer;
    public ArrayList<String> answers;

    public Detail() { }

    @Ignore
    public Detail(@NonNull String question) {
        this.question = question;
        this.uid = MyApplication.getUserKey();
        this.detailId = this.uid+this.question;
        answers = new ArrayList<>();
        if (question=="Education-Level") {
            answers.add("Preschool");
            answers.add("Elementary");
            answers.add("Middle-School");
            answers.add("High-School");
            answers.add("University");
        }
        if (question=="Gender") {
            answers.add("Male");
            answers.add("Female");
            answers.add("Don'tWishToSpecify");
        }
        if (question=="Age") {
            for (int i = 0; i < 100; i++) {
                answers.add(Integer.toString(i));
            }
        }
    }

    public Detail(@NonNull String question, String finalAnswer) {
        this.uid = MyApplication.getUserKey();
        this.detailId = uid+question;
        this.question = question;
        this.finalAnswer = finalAnswer;
        this.setAnswers(new ArrayList<>());
    }

    /**
     * Factory
     *
     */
    public static Detail create(Map<String, Object> data) {

        String question = (String)data.get("question");
        String answer = (String)data.get("answer");

        Detail detail = new Detail(question,answer);

        return detail;
    }

    @NonNull
    public String getUid() {
        return uid;
    }
    public void setUid(@NonNull String uid) {
        this.uid = uid;
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
        json.put("detail_id", detailId);
        json.put("uid",uid);
        json.put("question",question);
//        json.put("optional_answers", new JSONArray(answers));
        json.put("answer",finalAnswer);
        return json;
    }

}

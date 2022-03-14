package com.example.appproject.model.detail;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.appproject.MyApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
public class Detail {

    @PrimaryKey
    @NonNull
    public String detailId;
    public String userUid;
    public String questionId;
    public String question;
    public String answer;

    public Detail() { }

    @Ignore
    public Detail(@NonNull String questionId,String question) {
        this.questionId = questionId;
        this.question = question;
        this.userUid = MyApplication.getUserKey();
        this.detailId = UUID.randomUUID().toString();
    }

    @Ignore
    public Detail(@NonNull String questionId, String question, String finalAnswer) {
        this.userUid = MyApplication.getUserKey();
        this.detailId = UUID.randomUUID().toString();
        this.questionId = questionId;
        this.question = question;
        this.answer = finalAnswer;
    }

    /**
     * Factory
     *
     */
    public static Detail create(Map<String, Object> data) {
        String questionId = (String)data.get("question_id");
        String question = (String)data.get("question");
        String answer = (String)data.get("answer");

        return new Detail(questionId, question,answer);
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
    public void setDetailId(@NonNull String detailId) {
        this.detailId = detailId;
    }

    @NonNull
    public String getQuestionId() {
        return questionId;
    }
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
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
        json.put("question_id", questionId);
        json.put("question",question);
        json.put("answer", answer);
        return json;
    }

}

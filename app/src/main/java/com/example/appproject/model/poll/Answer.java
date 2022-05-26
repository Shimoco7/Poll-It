package com.example.appproject.model.poll;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.FieldValue;

import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
public class Answer {
    @PrimaryKey
    @NonNull
    public String answerId;
    @NonNull
    public String pollQuestionId;
    @NonNull
    public String pollId;
    @NonNull
    public String userId;
    public String answer;
    public Integer position;

    public Answer() { }

    @Ignore
    public Answer(@NonNull String userId,@NonNull String pollId, @NonNull String pollQuestionId, String answer, Integer position) {
        this.userId = userId;
        this.answerId = new ObjectId().toString();
        this.pollId = pollId;
        this.pollQuestionId = pollQuestionId;
        this.answer = answer;
        this.position = position;
    }

    public Map<String, String> toJson() {
        Map<String,String> json = new HashMap<>();
        json.put("_id",answerId);
        json.put("accountId",userId);
        json.put("pollId",pollId);
        json.put("pollQuestionId",pollQuestionId);
        json.put("answer", answer);
        return json;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getPollId() {
        return pollId;
    }

    public void setPollId(@NonNull String pollId) {
        this.pollId = pollId;
    }

    @NonNull
    public String getPollQuestionId() {
        return pollQuestionId;
    }

    public void setPollQuestionId(@NonNull String pollQuestionId) {
        this.pollQuestionId = pollQuestionId;
    }

    @NonNull
    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(@NonNull String answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}

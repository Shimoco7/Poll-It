package com.example.appproject.model.poll;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

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
    public Integer numOfPossibleOptions;
    public Double timeInSeconds = 0.0;

    public Answer() { }

    @Ignore
    public Answer(@NonNull String userId,@NonNull String pollId, @NonNull String pollQuestionId, String answer, Integer position, Integer numOfPossibleOptions) {
        this.userId = userId;
        this.answerId = new ObjectId().toString();
        this.pollId = pollId;
        this.pollQuestionId = pollQuestionId;
        this.answer = answer;
        this.position = position;
        this.numOfPossibleOptions = numOfPossibleOptions;
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

    public Double getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(Double timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public Integer getNumOfPossibleOptions() {
        return numOfPossibleOptions;
    }

    public void setNumOfPossibleOptions(Integer numOfPossibleOptions) {
        this.numOfPossibleOptions = numOfPossibleOptions;
    }
}

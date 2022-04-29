package com.example.appproject.model.poll;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.UUID;

@Entity
public class Poll {

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    public String pollId;
    public String pollName;
    public Integer totalNumberOfQuestions;

    public Poll() { }

    @Ignore
    public Poll(String pollName) {
        this.pollId = UUID.randomUUID().toString();
        this.pollName = pollName;
    }

    @Ignore
    public Poll(@NonNull String pollId, String pollName) {
        this.pollId = pollId;
        this.pollName = pollName;
    }


    @NonNull
    public String getPollId() {
        return pollId;
    }

    public void setPollId(@NonNull String pollId) {
        this.pollId = pollId;
    }

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public Integer getTotalNumberOfQuestions() {
        return totalNumberOfQuestions;
    }

    public void setTotalNumberOfQuestions(Integer totalNumberOfQuestions) {
        this.totalNumberOfQuestions = totalNumberOfQuestions;
    }
}


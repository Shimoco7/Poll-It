package com.example.appproject.model.poll;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.FieldValue;

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

    public Answer() {
    }

    @Ignore
    public Answer(@NonNull String answerId,@NonNull String userId,@NonNull String pollId, @NonNull String pollQuestionId, String answer) {
        this.answerId = answerId;
        this.userId = userId;
        this.pollId = pollId;
        this.pollQuestionId = pollQuestionId;
        this.answer = answer;
    }

    /**
     * Factory
     *
     */

    public static Answer create(Map<String, Object> data) {
        String answerId = (String)data.get("answer_id");
        String userId = (String)data.get("user_id");
        String pollId = (String)data.get("poll_id");
        String pollQuestionId = (String)data.get("poll_question_id");
        String answer = (String)data.get("answer");

        assert answerId != null;
        assert userId != null;
        assert pollId != null;
        assert answer != null;
        assert pollQuestionId != null;

        return new Answer(answerId,userId,pollId,pollQuestionId,answer);
    }

    public Map<String, Object> toJson() {
        Map<String,Object> json = new HashMap<>();
        json.put("answer_id",answerId);
        json.put("user_id",userId);
        json.put("poll_id",pollId);
        json.put("poll_question_id",pollQuestionId);
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
}

package com.example.appproject.model.poll;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Map;
import java.util.UUID;

@Entity
public class Answer {
    @PrimaryKey
    @NonNull
    public String answerId;
    @NonNull
    public String pollQuestionId;
    public String answer;

    public Answer() {
    }

    public Answer(@NonNull String pollQuestionId, String answer) {
        this.answerId = UUID.randomUUID().toString();
        this.pollQuestionId = pollQuestionId;
        this.answer = answer;
    }

    /**
     * Factory
     *
     */

    public static Answer create(Map<String, Object> data) {
        String answerId = (String)data.get("answer_id");
        String answer = (String)data.get("answer");
        String pollQuestionId = (String)data.get("poll_question_id");

        assert answerId != null;
        assert answer != null;
        assert pollQuestionId != null;

        return new Answer(pollQuestionId,answer);
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

package com.example.appproject.model.poll;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

enum PollQuestionType{
    @SerializedName("Multi Choice")
    Multi_Choice,
    @SerializedName("Image Answers")
    Image_Answers,
    @SerializedName("Image Question")
    Image_Question
}

@Entity
public class PollQuestion {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    public String pollQuestionId;
    @NonNull
    public String pollQuestion;
    @NonNull
    public String pollId;
    public String pollQuestionImage;
    public PollQuestionType pollQuestionType;
    public List<String> choices;

    public PollQuestion() { }

    @Ignore
    public PollQuestion(@NonNull String question,@NonNull String pollId) {
        this.pollQuestionId = UUID.randomUUID().toString();
        this.pollId = pollId;
        this.pollQuestion = question;
        choices = new ArrayList<>();
    }

    /**
     * Factory
     *
     */

    public static PollQuestion create(Map<String, Object> data) {
        String pollQuestionId = (String)data.get("poll_question_id");
        String pollQuestion = (String)data.get("poll_question");
        String pollId = (String)data.get("poll_id");
        List<String> choices = (List<String>)data.get("choices");

        assert pollQuestion != null;
        assert pollId != null;
        assert pollQuestionId != null;

        PollQuestion poll = new PollQuestion(pollQuestion,pollId);
        poll.setChoices(choices);
        poll.setPollQuestionId(pollQuestionId);

        return poll;
    }

    @NonNull
    public String getPollQuestionId() {
        return pollQuestionId;
    }

    public void setPollQuestionId(@NonNull String pollQuestionId) {
        this.pollQuestionId = pollQuestionId;
    }

    @NonNull
    public String getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(@NonNull String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    @NonNull
    public String getPollId() {
        return pollId;
    }

    public void setPollId(@NonNull String pollId) {
        this.pollId = pollId;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getPollQuestionImage() {
        return pollQuestionImage;
    }

    public void setPollQuestionImage(String pollQuestionImage) {
        this.pollQuestionImage = pollQuestionImage;
    }

    public PollQuestionType getPollQuestionType() {
        return pollQuestionType;
    }

    public void setPollQuestionType(PollQuestionType pollQuestionType) {
        this.pollQuestionType = pollQuestionType;
    }
}

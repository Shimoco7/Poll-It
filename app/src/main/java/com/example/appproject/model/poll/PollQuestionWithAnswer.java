package com.example.appproject.model.poll;

import androidx.room.Embedded;
import androidx.room.Relation;


public class PollQuestionWithAnswer {
    @Embedded
    public PollQuestion pollQuestion;
    @Relation(
            parentColumn = "pollQuestionId",
            entityColumn = "pollQuestionId"
    )
    public Answer answer;
}


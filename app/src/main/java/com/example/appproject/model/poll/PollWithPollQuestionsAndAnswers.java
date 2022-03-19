package com.example.appproject.model.poll;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PollWithPollQuestionsAndAnswers {
    @Embedded Poll poll;
    @Relation(
            entity = PollQuestion.class,
            parentColumn = "pollId",
            entityColumn = "pollId"
    )
    public List<PollQuestionWithAnswer> pollQuestionWithAnswers;
}

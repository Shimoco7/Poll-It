package com.example.appproject.model.poll;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PollWithQuestions {
    @Embedded
    public Poll poll;
    @Relation(
            parentColumn = "pollId",
            entityColumn = "pollId"
    )
    public List<PollQuestion> pollQuestions;
}

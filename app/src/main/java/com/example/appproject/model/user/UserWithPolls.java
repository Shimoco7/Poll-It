package com.example.appproject.model.user;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.appproject.model.poll.Poll;

import java.util.List;

public class UserWithPolls {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "uid",
            entityColumn = "pollId",
            associateBy = @Junction(UserPollCrossRef.class)
    )
    public List<Poll> polls;
}

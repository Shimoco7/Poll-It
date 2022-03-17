package com.example.appproject.model.poll;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.appproject.model.user.User;
import com.example.appproject.model.user.UserPollCrossRef;

import java.util.List;

public class PollWithUsers {
    @Embedded
    public Poll poll;
    @Relation(
            parentColumn = "pollId",
            entityColumn = "uid",
            associateBy = @Junction(UserPollCrossRef.class)
    )
    public List<User> users;
}

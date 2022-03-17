package com.example.appproject.model.user;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"uid","pollId"})
public class UserPollCrossRef {
    @NonNull
    public String uid;
    @NonNull
    public String pollId;
}

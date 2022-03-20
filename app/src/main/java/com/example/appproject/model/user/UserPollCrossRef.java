package com.example.appproject.model.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"uid","pollId"})
public class UserPollCrossRef {
    @NonNull
    public String uid;
    @NonNull
    @ColumnInfo(index = true)
    public String pollId;

    public UserPollCrossRef(@NonNull String uid, @NonNull String pollId) {
        this.uid = uid;
        this.pollId = pollId;
    }
}

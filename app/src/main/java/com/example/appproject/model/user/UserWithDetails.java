package com.example.appproject.model.user;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.example.appproject.model.detail.Detail;
import java.util.List;

public class UserWithDetails {
    @Embedded public User user;
    @Relation(
            parentColumn = "uid",
            entityColumn = "userUid"
    )
    public List<Detail> details;
}

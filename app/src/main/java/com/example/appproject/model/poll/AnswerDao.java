package com.example.appproject.model.poll;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Answer... answers);

    @Query("SELECT * FROM Answer WHERE userId=:uid AND pollId=:pollId")
    List<Answer> getAllAnswersByUserAndPollIds(String uid,String pollId);
}

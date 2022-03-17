package com.example.appproject.model.poll;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.appproject.model.user.User;

import java.util.List;

@Dao
public interface PollDao {

    @Query("SELECT * FROM Poll")
    List<Poll> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Poll... polls);

    @Transaction
    @Query("SELECT * FROM Poll WHERE pollId=:pollId")
    List<PollWithQuestions> getPollWithQuestions(String pollId);

    @Transaction
    @Query("SELECT * FROM PollQuestion WHERE pollQuestionId=:pollQuestionId")
    PollQuestionWithAnswer getPollQuestionWithAnswer(String pollQuestionId);
}

package com.example.appproject.model.poll;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PollQuestionDao {
    @Query("SELECT * FROM PollQuestion")
    List<PollQuestion> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(PollQuestion... pollQuestion);

    @Query("SELECT * FROM PollQuestion WHERE pollQuestionId = :pollQuestionId")
    PollQuestion getPollQuestionById(String pollQuestionId);

    @Query("SELECT * FROM PollQuestion WHERE pollid = :pollId ORDER BY questionNumber")
    List<PollQuestionWithAnswer> getPollQuestionsByPollId(String pollId);

}

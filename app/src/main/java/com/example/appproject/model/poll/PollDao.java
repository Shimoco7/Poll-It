package com.example.appproject.model.poll;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.appproject.model.user.User;
import com.example.appproject.model.user.UserPollCrossRef;
import com.example.appproject.model.user.UserWithPolls;

import java.util.List;

@Dao
public interface PollDao {

    @Query("SELECT * FROM Poll")
    List<Poll> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Poll... polls);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserPollCrossRef... userPollCrossRefs);

    @Transaction
    @Query("SELECT * FROM User WHERE uid=:uid")
    List<UserWithPolls> getUserWithPolls(String uid);

    @Transaction
    @Query("SELECT * FROM Poll WHERE pollId=:pollId")
    List<PollWithQuestions> getPollWithQuestions(String pollId);

    @Transaction
    @Query("SELECT * FROM PollQuestion WHERE pollQuestionId=:pollQuestionId")
    PollQuestionWithAnswer getPollQuestionWithAnswer(String pollQuestionId);

    @Transaction
    @Query("SELECT * FROM Poll WHERE pollId=:pollId")
    List<PollWithPollQuestionsAndAnswers> getPollWithPollQuestionsAndAnswers(String pollId);
}

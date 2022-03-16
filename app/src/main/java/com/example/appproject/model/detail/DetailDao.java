package com.example.appproject.model.detail;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface DetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Detail... details);

    @Query("SELECT * FROM detail WHERE userUid = :uid AND question = :question")
    Detail loadDetailByUserId(String uid, String question);

    @Query("UPDATE detail SET answer=:answer WHERE detailId = :detailId")
    void updateAnswerByDetailId(String detailId, String answer);

    @Query("SELECT * FROM detail WHERE userUid = :uid")
    List<Detail> getAllDetails(String uid);
}

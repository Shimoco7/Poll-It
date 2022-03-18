package com.example.appproject.model.question;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;




import java.util.List;
@Dao
public interface QuestionDao {
    @Query("SELECT * FROM Question")
    List<Question> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Question... questions);


}

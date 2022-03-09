package com.example.appproject.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAll();

    @Query("SELECT * FROM user WHERE uid = :uid")
    LiveData<User> loadUserById(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);


    @Delete
    void delete(User user);
}

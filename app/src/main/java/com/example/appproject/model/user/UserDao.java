package com.example.appproject.model.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("SELECT * FROM User WHERE uid = :uid")
    User loadUserById(String uid);

    @Transaction
    @Query("SELECT * FROM User WHERE uid=:uid")
    List<UserWithDetails> getUserWithDetails(String uid);

    @Transaction
    @Query("SELECT * FROM User WHERE uid=:uid")
    List<UserWithPolls> getUserWithPolls(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Delete
    void delete(User user);
}

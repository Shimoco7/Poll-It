package com.example.appproject.model.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.appproject.model.user.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    List<User> getAll();

    @Transaction
    @Query("SELECT * FROM User")
    List<UserWithDetails> getUsersWithDetails();

    @Query("SELECT * FROM user WHERE uid = :uid")
    User loadUserById(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Delete
    void delete(User user);
}

package com.example.appproject.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.appproject.MyApplication;

@Database(entities ={User.class},version =2,exportSchema = false)
abstract class AppLocalDbRepository extends RoomDatabase{
    public abstract UserDao userDao();
}

public class AppLocalDb {
    public static AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),AppLocalDbRepository.class,"PollItDb.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

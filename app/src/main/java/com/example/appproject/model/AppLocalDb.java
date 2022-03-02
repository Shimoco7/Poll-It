package com.example.appproject.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.appproject.MyApplication;

@Database(entities ={User.class},version =1)
abstract class AppLocalDbRepository extends RoomDatabase{

}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),AppLocalDbRepository.class,"PollItDb.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

package com.example.appproject.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.appproject.MyApplication;
import com.example.appproject.model.detail.Detail;
import com.example.appproject.model.detail.DetailDao;
import com.example.appproject.model.user.User;
import com.example.appproject.model.user.UserDao;


@Database(entities ={User.class, Detail.class},version = 16 ,exportSchema = false)
@TypeConverters({Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase{
    public abstract UserDao userDao();
    public abstract DetailDao detailDao();
}

public class AppLocalDb {
    public static AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),AppLocalDbRepository.class,"PollItDb.db")
                    .fallbackToDestructiveMigration()
                    .build();
}



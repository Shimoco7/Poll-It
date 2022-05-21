package com.example.appproject.model.reward;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface RewardDao {
    @Query("SELECT * FROM Reward")
    List<Reward> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Reward... rewards);

    @Query("SELECT * FROM Reward WHERE id = :rewardId")
    Reward getRewardById(String rewardId);
}

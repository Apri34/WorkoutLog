package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.Normal;

import java.util.List;

@Dao
public interface NormalDao {
    @Query("SELECT * FROM Normal")
    List<Normal> getAll();
}

package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.Dropset;

import java.util.List;

@Dao
public interface DropsetDao {
    @Query("SELECT * FROM Dropset")
    List<Dropset> getAll();
}

package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.SetDone;

import java.util.List;

@Dao
public interface SetDoneDao {
    @Query("SELECT * FROM SetDone")
    List<SetDone> getAll();
}

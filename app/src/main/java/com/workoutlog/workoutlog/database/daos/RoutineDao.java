package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.Routine;

import java.util.List;

@Dao
public interface RoutineDao {
    @Query("SELECT * FROM Routine")
    List<Routine> getAll();
}

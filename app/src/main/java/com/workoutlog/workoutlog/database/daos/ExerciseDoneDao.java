package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.ExerciseDone;

import java.util.List;

@Dao
public interface ExerciseDoneDao {
    @Query("SELECT * FROM ExerciseDone")
    List<ExerciseDone> getAll();

    @Insert
    void insertExerciseDone(ExerciseDone exerciseDone);
}

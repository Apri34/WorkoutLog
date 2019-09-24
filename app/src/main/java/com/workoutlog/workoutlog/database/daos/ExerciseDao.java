package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM Exercise")
    List<Exercise> getAll();

    @Insert
    void insertExercise(Exercise exercise);
}

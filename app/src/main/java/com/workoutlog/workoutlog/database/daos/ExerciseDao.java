package com.workoutlog.workoutlog.database.daos;

import androidx.room.*;
import com.workoutlog.workoutlog.database.entities.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM Exercise ORDER BY E_ID ASC")
    List<Exercise> getAll();

    @Query("SELECT E_Name FROM Exercise")
    List<String> getAllExerciseNames();

    @Insert
    void insertExercise(Exercise exercise);

    @Query("UPDATE Exercise SET E_Name = :eName WHERE E_ID = :eId")
    void updateExercise(int eId, String eName);

    @Delete
    void deleteExercise(Exercise exercise);
}

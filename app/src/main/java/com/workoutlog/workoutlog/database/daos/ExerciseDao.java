package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.workoutlog.workoutlog.database.entities.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM Exercise")
    List<Exercise> getAll();

    @Query("SELECT E_Name FROM Exercise")
    List<String> getAllExerciseNames();

    @Insert
    void insertExercise(Exercise exercise);

    @Query("UPDATE Exercise SET E_Name = :eName WHERE E_ID = :eId")
    void updateExercise(int eId, String eName);
}

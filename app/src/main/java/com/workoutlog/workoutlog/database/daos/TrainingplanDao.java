package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.Trainingplan;

import java.util.List;

@Dao
public interface TrainingplanDao {
    @Query("SELECT * FROM Trainingplan")
    List<Trainingplan> getAll();

    @Insert
    void insertTrainingplan(Trainingplan trainingplan);
}

package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.Trainingplan;

import java.util.List;

@Dao
public interface TrainingplanDao {
    @Query("SELECT * FROM Trainingplan WHERE TP_ID >= 0")
    List<Trainingplan> getAll();

    @Query("SELECT TP_Name FROM Trainingplan")
    List<String> getAllTrainingplanNames();

    @Query("SELECT * FROM Trainingplan ORDER BY TP_ID DESC LIMIT 1")
    Trainingplan getLastTrainingplan();

    @Query("SELECT * FROM Trainingplan WHERE TP_ID = :tpId")
    Trainingplan getTrainingplanById(int tpId);

    @Insert
    void insertTrainingplan(Trainingplan trainingplan);

    @Delete
    void deleteTrainingplan(Trainingplan trainingplan);

    @Query("UPDATE Trainingplan SET TP_Name = :tpName WHERE TP_ID = :tpId")
    void updateTrainingplan(int tpId, String tpName);
}

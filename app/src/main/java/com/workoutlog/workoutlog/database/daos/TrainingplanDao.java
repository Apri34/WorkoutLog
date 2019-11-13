package com.workoutlog.workoutlog.database.daos;

import androidx.room.*;
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrainingplan(Trainingplan trainingplan);

    @Delete
    void deleteTrainingplan(Trainingplan trainingplan);

    @Query("UPDATE Trainingplan SET TP_Name = :tpName WHERE TP_ID = :tpId")
    void updateTrainingplan(int tpId, String tpName);

    @Query("SELECT count(*) FROM Trainingplan")
    int getCount();
}

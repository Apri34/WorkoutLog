package com.workoutlog.workoutlog.database.daos;

import androidx.room.*;
import com.workoutlog.workoutlog.database.entities.Normal;

import java.util.List;

@Dao
public interface NormalDao {
    @Query("SELECT * FROM Normal")
    List<Normal> getAll();

    @Query("SELECT * FROM Normal WHERE R_ID = :rId")
    List<Normal> getNormalsByRoutineId(int rId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNormal(Normal normal);

    @Delete
    void deleteNormal(Normal normal);

    @Query("SELECT * FROM Normal WHERE E_ID = :eId")
    List<Normal> getNormalsByEId(int eId);

    @Query("SELECT count(*) FROM Normal")
    int getCount();

    @Query("SELECT * FROM Normal ORDER BY N_ID DESC LIMIT 1")
    Normal getLastNormal();
}

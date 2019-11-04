package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.Normal;

import java.util.List;

@Dao
public interface NormalDao {
    @Query("SELECT * FROM Normal")
    List<Normal> getAll();

    @Query("SELECT * FROM Normal WHERE R_ID = :rId")
    List<Normal> getNormalsByRoutineId(int rId);

    @Insert
    void insertNormal(Normal normal);

    @Delete
    void deleteNormal(Normal normal);

    @Query("SELECT * FROM Normal WHERE E_ID = :eId")
    List<Normal> getNormalsByEId(int eId);

    @Query("SELECT * FROM Normal WHERE E_ID = :eId AND R_ID = :rId")
    Normal getNormalByEIdAndRId(int eId, int rId);
}

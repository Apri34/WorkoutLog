package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.Dropset;

import java.util.List;

@Dao
public interface DropsetDao {
    @Query("SELECT * FROM Dropset")
    List<Dropset> getAll();

    @Query("SELECT * FROM Dropset WHERE R_ID = :rId")
    List<Dropset> getDropsetsByRoutineId(int rId);

    @Insert
    void insertDropset(Dropset dropset);

    @Delete
    void deleteDropset(Dropset dropset);

    @Query("SELECT * FROM Dropset WHERE E_ID = :eId")
    List<Dropset> getDropsetsByEId(int eId);
}

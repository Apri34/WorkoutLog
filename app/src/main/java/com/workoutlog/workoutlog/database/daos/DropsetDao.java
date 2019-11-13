package com.workoutlog.workoutlog.database.daos;

import androidx.room.*;
import com.workoutlog.workoutlog.database.entities.Dropset;

import java.util.List;

@Dao
public interface DropsetDao {
    @Query("SELECT * FROM Dropset")
    List<Dropset> getAll();

    @Query("SELECT * FROM Dropset WHERE R_ID = :rId")
    List<Dropset> getDropsetsByRoutineId(int rId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDropset(Dropset dropset);

    @Delete
    void deleteDropset(Dropset dropset);

    @Query("SELECT * FROM Dropset WHERE E_ID = :eId")
    List<Dropset> getDropsetsByEId(int eId);

    @Query("SELECT count(*) FROM Dropset")
    int getCount();

    @Query("SELECT * FROM Dropset ORDER BY D_ID DESC LIMIT 1")
    Dropset getLastDropset();
}

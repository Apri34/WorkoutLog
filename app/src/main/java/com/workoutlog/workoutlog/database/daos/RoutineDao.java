package com.workoutlog.workoutlog.database.daos;

import androidx.room.*;
import com.workoutlog.workoutlog.database.entities.Routine;

import java.util.List;

@Dao
public interface RoutineDao {
    @Query("SELECT * FROM Routine")
    List<Routine> getAll();

    @Query("SELECT * FROM Routine WHERE TP_ID = :tpId ORDER BY Pos_In_TP ASC")
    List<Routine> getRoutinesByTpId(int tpId);

    @Query("SELECT R_Name FROM Routine WHERE TP_ID = :tpId ORDER BY Pos_In_TP ASC")
    List<String> getRoutineNamesByTpId(int tpId);

    @Query("SELECT count(*) FROM Routine WHERE TP_ID = :tpId")
    int getNumberRoutinesInTp(int tpId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRoutine(Routine routine);

    @Query("SELECT * FROM Routine ORDER BY R_ID DESC LIMIT 1")
    Routine getLastRoutine();

    @Delete
    void deleteRoutine(Routine routine);

    @Query("SELECT * FROM Routine WHERE R_ID = :rId")
    Routine getRoutineById(int rId);

    @Query("UPDATE Routine SET R_Name = :rName WHERE R_ID = :rId")
    void updateRoutine(int rId, String rName);

    @Query("SELECT count(*) FROM Routine")
    int getCount();
}

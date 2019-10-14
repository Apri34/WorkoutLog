package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.Superset;

import java.util.List;

@Dao
public interface SupersetDao {
    @Query("SELECT * FROM Superset")
    List<Superset> getAll();

    @Query("SELECT * FROM Superset WHERE R_ID = :rId")
    List<Superset> getSupersetsByRoutineId(int rId);

    @Insert
    void insertSuperset(Superset superset);

    @Delete
    void deleteSuperset(Superset superset);

    @Query("SELECT * FROM Superset WHERE E_ID1 = :eId OR E_ID2 = :eId")
    List<Superset> getSupersetsByEId(int eId);
}

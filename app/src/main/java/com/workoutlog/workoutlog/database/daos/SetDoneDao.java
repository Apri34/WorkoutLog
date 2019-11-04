package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.SetDone;

import java.util.List;

@Dao
public interface SetDoneDao {
    @Query("SELECT * FROM SetDone")
    List<SetDone> getAll();

    @Insert
    void insertSetDone(SetDone setDone);

    @Query("SELECT * FROM SetDone WHERE ED_ID = :edId ORDER BY Pos_In_Exc_Done ASC")
    List<SetDone> getSetDonesByEdId(int edId);
}

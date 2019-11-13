package com.workoutlog.workoutlog.database.daos;

import androidx.room.*;
import com.workoutlog.workoutlog.database.entities.SetDone;

import java.util.List;

@Dao
public interface SetDoneDao {
    @Query("SELECT * FROM SetDone")
    List<SetDone> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSetDone(SetDone setDone);

    @Query("SELECT * FROM SetDone WHERE ED_ID = :edId ORDER BY Pos_In_Exc_Done ASC")
    List<SetDone> getSetDonesByEdId(int edId);

    @Query("SELECT count(*) FROM SetDone")
    int getCount();

    @Delete
    void deleteSetDone(SetDone setDone);

    @Query("SELECT * FROM SetDone ORDER BY SD_ID DESC LIMIT 1")
    SetDone getLastSetDone();
}

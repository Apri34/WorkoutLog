package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.Superset;

import java.util.List;

@Dao
public interface SupersetDao {
    @Query("SELECT * FROM Superset")
    List<Superset> getAll();
}

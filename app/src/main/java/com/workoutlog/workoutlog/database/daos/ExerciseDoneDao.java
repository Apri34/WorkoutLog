package com.workoutlog.workoutlog.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.workoutlog.workoutlog.database.entities.ExerciseDone;

import java.sql.Date;
import java.util.List;

@Dao
public interface ExerciseDoneDao {
    @Query("SELECT * FROM ExerciseDone")
    List<ExerciseDone> getAll();

    @Insert
    void insertExerciseDone(ExerciseDone exerciseDone);

    @Query("SELECT * FROM ExerciseDone WHERE E_ID = :eId ORDER BY Date DESC")
    List<ExerciseDone> getExerciseDonesByEId(int eId);

    @Query("SELECT count(*) FROM ExerciseDone WHERE Date between :startDate AND :endDate")
    int getNumberOnDate(Date startDate, Date endDate);

    @Query("SELECT ED_ID FROM ExerciseDone ORDER BY ED_ID DESC LIMIT 1")
    int getLastEdId();

    @Query("SELECT * FROM ExerciseDone WHERE Date between :startDate AND :endDate ORDER BY Pos_On_Date ASC")
    List<ExerciseDone> getExerciseDonesByDate(Date startDate, Date endDate);

    @Query("SELECT count(*) FROM ExerciseDone WHERE Date > :date")
    int getNumExerciseDonesFromDate(Date date);

    @Query("SELECT count(*) FROM ExerciseDone WHERE Date < :date")
    int getNumExerciseDonesUntilDate(Date date);
}
